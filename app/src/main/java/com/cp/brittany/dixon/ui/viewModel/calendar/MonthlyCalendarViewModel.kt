package com.cp.brittany.dixon.ui.viewModel.calendar

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.data.calendar.CalendarUIState
import com.cp.brittany.dixon.data.calendar.MonthlyCalendarUIEvent
import com.cp.brittany.dixon.data.calendar.MonthlyCalendarUIState
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
class MonthlyCalendarViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()


) : ViewModel() {
    private val _getScheduleSlotsResponse: MutableStateFlow<NetworkResult<WorkoutByCategoryModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val getScheduleSlotsResponse: StateFlow<NetworkResult<WorkoutByCategoryModel>> =
        _getScheduleSlotsResponse


    private var calendarUIState = mutableStateOf(MonthlyCalendarUIState())

    var getAvailableSlotsForCalendar = SnapshotStateList<WorkoutData>()

    fun onEvent(event: MonthlyCalendarUIEvent) {
        when (event) {
            is MonthlyCalendarUIEvent.SelectStartEndDateFromCalendar -> {
                calendarUIState.value = calendarUIState.value.copy(
                    startDate = event.startDate,
                    endDate = event.eneDate
                )
                getScheduleSlots(
                    calendarUIState.value.startDate,
                    calendarUIState.value.endDate
                )
            }

            else -> {

            }
        }
    }

    fun getScheduleSlots(startDate: String, endDate: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (applicationContext.isNetworkAvailable()) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("start_date", startDate)
                jsonObject.addProperty("end_date", endDate)
                Log.d("xldldkckcmscm", startDate)
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

    fun resetResponse() {
        _getScheduleSlotsResponse.value = NetworkResult.NoCallYet()
    }

}


