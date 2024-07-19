package com.cp.brittany.dixon.ui.viewModel.calendar

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.ScheduleWorkoutDetailUIEvent
import com.cp.brittany.dixon.data.workout.WorkoutDetailUIEvent
import com.cp.brittany.dixon.data.workout.WorkoutUIState
import com.cp.brittany.dixon.model.calendar.Images
import com.cp.brittany.dixon.model.favourite.AddFavourite
import com.cp.brittany.dixon.model.workout.WorkoutByCategoryModel
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleWorkoutDetailViewModel @Inject constructor(
    val preference: SharePreferenceHelper,

    ) : ViewModel() {

    var imagesList = mutableStateOf(WorkoutData())
    var getScheduleWorkoutsDetailData = mutableStateOf(false)


    init {
        getScheduleWorkoutsDetailData.value = true
    }


    fun deleteImage(id: Int) {
        imagesList.value.images?.forEachIndexed { index, images ->
            if (images.id == id) {
                val currentState = imagesList.value
                val item = currentState.images?.toMutableList()?.apply {
                    remove(images)
                }
                imagesList.value = imagesList.value.copy(
                    images = item
                )
                return@forEachIndexed
            }
        }
    }
}


