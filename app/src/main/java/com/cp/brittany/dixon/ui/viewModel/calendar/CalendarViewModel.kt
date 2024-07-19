package com.cp.brittany.dixon.ui.viewModel.calendar

import android.app.Application
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Environment
import android.webkit.URLUtil
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.data.calendar.CalendarUIState
import com.cp.brittany.dixon.model.calendar.Images
import com.cp.brittany.dixon.model.calendar.ScheduleModel
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.composecalendar.CalendarDataSource
import com.cp.brittany.dixon.utills.compressMyImage
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {
    private val _workoutByDatesResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val workoutByDatesResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _workoutByDatesResponse

    private val _getScheduleSlotsResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getScheduleSlotsResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _getScheduleSlotsResponse

    private val _workoutByPreviousDatesResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val workoutByPreviousDatesResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _workoutByPreviousDatesResponse

    private val _addMultipleImageResponse: MutableStateFlow<NetworkResult<ScheduleModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addMultipleImageResponse: StateFlow<NetworkResult<ScheduleModel>> =
        _addMultipleImageResponse.asStateFlow()

    var showNoDataFoundOrNot by mutableStateOf(false)

    var showNoDataFoundIfHasTodayWorkouts by mutableStateOf(false)
    var checkTodayScheduleWorkoutsAvailableOrNot by mutableStateOf(false)
    var workoutByDateLoaderState by mutableStateOf(false)


    var monthDates = mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today))

    var getScheduleWorkoutsResponse = mutableStateOf(false)
    var visibleScheduleItems = mutableStateOf(false)
    var visibleTodayScheduleItems = mutableStateOf(false)
    var getAllScheduledWorkoutsResponse = mutableStateOf(false)
    var getScheduleWorkoutsSlotsResponse = mutableStateOf(false)
    var showScheduleWorkoutList = mutableStateOf(false)
    var indexValue = mutableStateOf(0)
    var scheduleWorkoutResponseData = SnapshotStateList<WorkoutData>()
    var previousScheduleWorkoutResponseData = mutableListOf<WorkoutData>()
    var getAvailableSlots = mutableListOf<WorkoutData>()
    val offsetX1 = Animatable(Constants.OFF_SET_X_1)
    val offsetY1 = Animatable(Constants.OFF_SET_Y_1)

    val offsetX2 = Animatable(Constants.OFF_SET_X_1)
    val offsetY2 = Animatable(Constants.OFF_SET_Y_2)

    val offsetX3 = Animatable(Constants.OFF_SET_X_3)
    val offsetY3 = Animatable(Constants.OFF_SET_Y_3)
    private var calendarUIState = mutableStateOf(CalendarUIState())

    init {
        workoutByDates()
    }

    fun onEvent(event: CalendarUIEvent) {
        when (event) {
            is CalendarUIEvent.SelectDateFromWeekCalendar -> {
                calendarUIState.value = calendarUIState.value.copy(
                    date = event.date
                )
                getAllScheduledWorkoutsResponse.value = true
                showScheduleWorkoutList.value = false
                workoutByPreviousDates(calendarUIState.value.date)
            }

            is CalendarUIEvent.GetImages -> {
                calendarUIState.value = calendarUIState.value.copy(
                    getImages = event.images,
                    workoutId = event.workoutId
                )
                addUserImages()
            }

            is CalendarUIEvent.RefreshData -> {
                workoutByDates()
            }
        }
    }

    fun updateSelection(index: Int, images: ArrayList<Images>) {
        val item = scheduleWorkoutResponseData[index]
//        if ((scheduleWorkoutResponseData[index].images?.size ?: 0) <= 5) {
        scheduleWorkoutResponseData[index] = item.copy(images = images)
//        }
    }

    fun workoutByDates() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val today = LocalDate.now()
            val jsonObject = JsonObject()
            jsonObject.addProperty("date", today.toString())
            _workoutByDatesResponse.value = NetworkResult.Loading()
            repository.getByDatesWorkouts(jsonObject).collect { response ->
                getScheduleSlots()
                response.apply {
                    _workoutByDatesResponse.value = response
                }
            }
        } else {
            _workoutByDatesResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    private fun getScheduleSlots() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("start_date", monthDates.value.startDate.date.toString())
            jsonObject.addProperty("end_date", monthDates.value.endDate.date.toString())
            _getScheduleSlotsResponse.value = NetworkResult.Loading()
            repository.getScheduleSlots(jsonObject).collect { response ->
                workoutByPreviousDates(LocalDate.now().toString())
                response.apply {
                    _getScheduleSlotsResponse.value = response
                }
            }
        } else {
            _getScheduleSlotsResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    private fun workoutByPreviousDates(date: String) = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("date", date)
            jsonObject.addProperty("limit", 5)
            _workoutByPreviousDatesResponse.value = NetworkResult.Loading()
            repository.getByDatesWorkouts(jsonObject).collect { response ->
                response.apply {
                    _workoutByPreviousDatesResponse.value = response
                }
            }
        } else {
            _workoutByPreviousDatesResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }


    private fun addUserImages() {
        if (applicationContext.isNetworkAvailable()) {
            viewModelScope.launch {
                _addMultipleImageResponse.value = NetworkResult.Loading()
                val multipartBodyList = ArrayList<MultipartBody.Part>()
                calendarUIState.value.getImages?.forEachIndexed { index, picture ->

                    val multipartBody: MultipartBody.Part? = if (!URLUtil.isValidUrl(picture)) {
                        val file = File(picture)
                        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData(
                            "images[$index]", file.name, requestFile
                        )
                    } else
                        null
                    if (multipartBody != null) {
                        multipartBodyList.add(multipartBody)
                    }

//                    val multipartBody: MultipartBody.Part? = if (!URLUtil.isValidUrl(picture)) {
//                        val file = if (Utils.isNeedToCompressImage(picture)) {
//                            val bitmap = BitmapFactory.decodeFile(picture)
//                            val timeStamp: String =
//                                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                            val imageFileName = "JPEG_" + timeStamp + "_"
//                            val sd_main =
//                                File(Environment.getExternalStorageDirectory(), "/Pictures")
//                            val newFile = bitmap.compressMyImage(sd_main, imageFileName)
//                            val oldExif = ExifInterface(picture)
//                            val exifOrientation =
//                                oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)
//                            if (exifOrientation != null) {
//                                val newExif = ExifInterface(newFile?.path ?: "")
//                                newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
//                                newExif.saveAttributes()
//                            }
//                            newFile
//                        } else {
//                            File(picture)
//                        }
//                        Utils.fileSize(file)
//
//                        if (file != null) {
//                            val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
//                            MultipartBody.Part.createFormData(
//                                "images[$index]", file.name, requestFile
//                            )
//                        } else {
//                            null
//                        }
//                    } else null
//
//                    if (multipartBody != null) {
//                        multipartBodyList.add(multipartBody)
//                    }

                }

                repository.uploadImages(
                    pictures = multipartBodyList,
                    workoutId = calendarUIState.value.workoutId.toString().toRequestBody()
                ).collect { values ->
                    _addMultipleImageResponse.value = values
                }
            }
        } else {
            _addMultipleImageResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    fun resetResponse() {
        _workoutByDatesResponse.value = NetworkResult.NoCallYet()
        _workoutByPreviousDatesResponse.value = NetworkResult.NoCallYet()
        _getScheduleSlotsResponse.value = NetworkResult.NoCallYet()
        _addMultipleImageResponse.value = NetworkResult.NoCallYet()
    }

}


