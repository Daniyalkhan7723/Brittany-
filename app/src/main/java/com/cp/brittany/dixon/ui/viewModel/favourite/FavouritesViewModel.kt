package com.cp.brittany.dixon.ui.viewModel.favourite

import android.app.Application
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.data.calendar.CalendarUIState
import com.cp.brittany.dixon.model.favourite.GetInsideFavourites
import com.cp.brittany.dixon.model.favourite.GetWorkoutFavourites
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.insight.InsightModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.Constants
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
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {


    var segmentState = mutableStateOf(0)
    val items = mutableStateListOf("Workouts", "Insights")


}


