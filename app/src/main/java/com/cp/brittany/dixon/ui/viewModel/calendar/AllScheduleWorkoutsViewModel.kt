package com.cp.brittany.dixon.ui.viewModel.calendar

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIState
import com.cp.brittany.dixon.data.calendar.MonthlyCalendarUIEvent
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.composecalendar.CalendarDataSource
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AllScheduleWorkoutsViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()


) : ViewModel() {

    var scheduleWorkoutResponseData = mutableListOf<WorkoutData>()

    private val _workoutByPreviousDatesResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val workoutByPreviousDatesResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _workoutByPreviousDatesResponse

    private val _getScheduleSlotsResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getScheduleSlotsResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _getScheduleSlotsResponse

    private val _getScheduleSlotsForCalendarResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getScheduleSlotsCalendarResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _getScheduleSlotsForCalendarResponse

    var getAvailableSlots = mutableListOf<WorkoutData>()
    var getAvailableSlotsForCalendar = SnapshotStateList<WorkoutData>()
    var visibleScheduleItems = mutableStateOf(false)
    var getScheduleWorkoutsSlotsResponse = mutableStateOf(false)  // For avoid recomposition
    var getScheduleWorkoutsResponse = mutableStateOf(false)      // For avoid recomposition
    var workoutByDateLoaderState by mutableStateOf(false)

    //    var preventAgainApiCall = mutableStateOf(false)      // For avoid recomposition
    private var calendarUIState = mutableStateOf(CalendarUIState())
    var preventAgainApiCall = false

    var monthDates = mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today))

    init {
        workoutByPreviousDates(LocalDate.now().toString())
        val date = dataSource.getData(lastSelectedDate = dataSource.today)
        val startDate = date.startDate.date.toString()
        val endDate = date.endDate.date.toString()
        getScheduleSlots(startDate, endDate)
    }

    fun onEvent(event: MonthlyCalendarUIEvent) {
        when (event) {
            is MonthlyCalendarUIEvent.SelectDateFromWeekCalendar -> {
                calendarUIState.value = calendarUIState.value.copy(
                    date = event.date
                )
                workoutByPreviousDates(calendarUIState.value.date)
            }

            is MonthlyCalendarUIEvent.SelectStartEndDateFromCalendar -> {
                event.apply {
                    calendarUIState.value = calendarUIState.value.copy(
                        startDate = startDate,
                        endDate = eneDate
                    )
                    getScheduleSlotsForCalendar(
                        calendarUIState.value.startDate,
                        calendarUIState.value.endDate
                    )
                }

            }

            is MonthlyCalendarUIEvent.GetSlotsForWeekCalendar -> {
                event.apply {
                    calendarUIState.value = calendarUIState.value.copy(
                        startDateForWeekCalendar = startDate,
                        endDateForWeekCalendar = eneDate
                    )
                    getScheduleWorkoutsSlotsResponse.value = true
                    getScheduleSlots(
                        calendarUIState.value.startDateForWeekCalendar,
                        calendarUIState.value.endDateForWeekCalendar
                    )

                }

            }

            is MonthlyCalendarUIEvent.RefreshData -> {
                workoutByPreviousDates(LocalDate.now().toString())
                val date = dataSource.getData(lastSelectedDate = dataSource.today)
                val startDate = date.startDate.date.toString()
                val endDate = date.endDate.date.toString()
                getScheduleSlots(startDate, endDate)
            }
        }
    }

    private fun getScheduleSlots(startDate: String, endDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (applicationContext.isNetworkAvailable()) {
//                val date = dataSource.getData(lastSelectedDate = dataSource.today)
//                val startDate = date.startDate.date.toString()
//                val endDate = date.endDate.date.toString()
                val jsonObject = JsonObject().apply {
                    addProperty("start_date", startDate)
                    addProperty("end_date", endDate)
                }
                _getScheduleSlotsResponse.value = NetworkResult.Loading()
                repository.getScheduleSlots(jsonObject).collect { response ->
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

    private fun getScheduleSlotsForCalendar(startDate: String, endDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (applicationContext.isNetworkAvailable()) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("start_date", startDate)
                jsonObject.addProperty("end_date", endDate)

                _getScheduleSlotsForCalendarResponse.value = NetworkResult.Loading()
                repository.getScheduleSlots(jsonObject).collect { response ->
                    response.apply {
//                        response.data?.data?.let { getAvailableSlotsForCalendar.addAll(it) }
                        _getScheduleSlotsForCalendarResponse.value = response
                    }
                }
            } else {
                _getScheduleSlotsForCalendarResponse.value = NetworkResult.NoInternet(
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


    fun resetResponse() {
        _workoutByPreviousDatesResponse.value = NetworkResult.NoCallYet()
        _getScheduleSlotsResponse.value = NetworkResult.NoCallYet()
        _getScheduleSlotsForCalendarResponse.value = NetworkResult.NoCallYet()
    }

}


