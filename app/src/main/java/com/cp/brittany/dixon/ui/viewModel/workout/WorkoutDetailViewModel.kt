package com.cp.brittany.dixon.ui.viewModel.workout

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.workout.WorkoutDetailUIEvent
import com.cp.brittany.dixon.data.workout.WorkoutUIState
import com.cp.brittany.dixon.model.calendar.ScheduleModel
import com.cp.brittany.dixon.model.favourite.AddFavourite
import com.cp.brittany.dixon.model.workout.StartWorkoutResponse
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.model.workout.WorkoutDetailsResponse
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.Utils.convertStringToArrayList
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    private val stateHandle: SavedStateHandle,
) : ViewModel() {

    private val _categoryWorkoutResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val categoryWorkoutResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _categoryWorkoutResponse

    private val _addFavouriteResponse: MutableStateFlow<NetworkResult<AddFavourite>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addFavouriteResponse: StateFlow<NetworkResult<AddFavourite>> = _addFavouriteResponse

    private val _workoutDetail: MutableStateFlow<NetworkResult<WorkoutDetailsResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val workoutDetail: StateFlow<NetworkResult<WorkoutDetailsResponse>> = _workoutDetail

    private val _startWorkoutResponse: MutableStateFlow<NetworkResult<StartWorkoutResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val startWorkoutResponse: StateFlow<NetworkResult<StartWorkoutResponse>> = _startWorkoutResponse

    private val _addScheduleResponse: MutableStateFlow<NetworkResult<ScheduleModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addScheduleResponse: StateFlow<NetworkResult<ScheduleModel>> = _addScheduleResponse

    var categoryWorkoutResponseData = mutableListOf<WorkoutData>()
    var workoutUIState = mutableStateOf(WorkoutUIState())

    var handleStartWorkoutRecomposition by mutableStateOf(false)


    var favouriteImageState by
    mutableStateOf(false)

    var isFeaturedWorkout by
    mutableStateOf(false)

    var workoutDetailObject by mutableStateOf(WorkoutData())

    var loaderState by mutableStateOf(false)


    init {
        workoutDetail()
        if (stateHandle.get<String>(Constants.SCREEN_TYPE) == Constants.WORKOUTS) {
            randomWorkoutByCategory()
        }

    }


    fun onEvent(event: WorkoutDetailUIEvent) {
        when (event) {
            is WorkoutDetailUIEvent.AddFavouriteChanged -> {
                workoutUIState.value = workoutUIState.value.copy(
                    workoutId = event.id
                )
                addFavourite()
            }

            is WorkoutDetailUIEvent.AddScheduleChanged -> {
                workoutUIState.value = workoutUIState.value.copy(
                    selectedDates = event.dates,
                    workoutId = event.id
                )
                schedule()
            }

            is WorkoutDetailUIEvent.WorkoutStart -> {
                workoutUIState.value = workoutUIState.value.copy(
                    workoutId = event.workoutId,
                    workoutVideoId = event.workoutVideoId,
                )
                handleStartWorkoutRecomposition = true
                startWorkout()
            }


            is WorkoutDetailUIEvent.RefreshDetails -> {
                workoutDetail()
            }
        }
    }

    private fun randomWorkoutByCategory() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val categoryId = stateHandle.get<String>(Constants.CATEGORY_ID)
            val jsonObject = JsonObject()
            jsonObject.addProperty("category_id", categoryId?.toInt())
            jsonObject.addProperty("limit", 5)

            _categoryWorkoutResponse.value = NetworkResult.Loading()
            repository.randomWorkoutByCategory(jsonObject).collect { response ->
                response.apply {
                    _categoryWorkoutResponse.value = response
                }
            }
        } else {
            _categoryWorkoutResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }

    private fun addFavourite() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("workout_id", workoutUIState.value.workoutId)
            _addFavouriteResponse.value = NetworkResult.Loading()
            repository.addFavourite(jsonObject).collect { response ->
                response.apply {
                    _addFavouriteResponse.value = response
                }
            }
        } else {
            _addFavouriteResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }

    private fun workoutDetail() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("workout_id", stateHandle.get<String>(Constants.WORKOUT_ID))
            _workoutDetail.value = NetworkResult.Loading()
            repository.workoutDetail(jsonObject).collect { response ->
                response.apply {
                    _workoutDetail.value = response
                }
            }
        } else {
            _workoutDetail.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    private fun startWorkout() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("workout_id", stateHandle.get<String>(Constants.WORKOUT_ID))
            jsonObject.addProperty(
                "workout_video_id",
                workoutUIState.value.workoutVideoId
            )
            jsonObject.addProperty("watched_time", "00:00:1")

            _startWorkoutResponse.value = NetworkResult.Loading()
            repository.startWorkout(jsonObject).collect { response ->
                response.apply {
                    _startWorkoutResponse.value = response
                }
            }
        } else {
            _startWorkoutResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    private fun schedule() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {

            var datesArray = convertStringToArrayList(workoutUIState.value.selectedDates ?: "")
            val jsonObject = JsonObject()
            jsonObject.addProperty("workout_id", workoutUIState.value.workoutId)
            jsonObject.add(
                "date", Gson().toJsonTree(
                    datesArray
                )
            )
            _addScheduleResponse.value = NetworkResult.Loading()
            repository.addSchedule(jsonObject).collect { response ->
                response.apply {
                    _addScheduleResponse.value = response
                }
            }
        } else {
            _addScheduleResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    fun resetResponse() {
        _categoryWorkoutResponse.value = NetworkResult.NoCallYet()
        _addFavouriteResponse.value = NetworkResult.NoCallYet()
        _addScheduleResponse.value = NetworkResult.NoCallYet()
        _workoutDetail.value = NetworkResult.NoCallYet()
        _startWorkoutResponse.value = NetworkResult.NoCallYet()
    }

}


