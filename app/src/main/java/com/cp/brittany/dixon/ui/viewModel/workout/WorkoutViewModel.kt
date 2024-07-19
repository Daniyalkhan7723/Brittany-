package com.cp.brittany.dixon.ui.viewModel.workout

import android.app.Application
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.workout.WorkoutUIEvent
import com.cp.brittany.dixon.model.workout.Data
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.model.workout.WorkoutModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor
    (
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
) : ViewModel() {
    private val _featureWorkoutResponse: MutableStateFlow<NetworkResult<FeatureWorkoutModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val featureWorkoutResponse: StateFlow<NetworkResult<FeatureWorkoutModel>> =
        _featureWorkoutResponse
    var visibleTutorial by mutableStateOf(false)


    private val _categoryWorkoutResponse: MutableStateFlow<NetworkResult<WorkoutModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val categoryWorkoutResponse: StateFlow<NetworkResult<WorkoutModel>> = _categoryWorkoutResponse

    var getFeatureWorkoutResponseData = mutableStateOf(WorkoutData())

    var visibleFeaturedWorkout = mutableStateOf(false)
    var visibleCategoryWorkout = mutableStateOf(false)
    var workoutLoaderState = mutableStateOf(false)
    var categoryWorkoutResponseData = mutableListOf<Data>()


    val alpha =
        Animatable(0.1f)


    val offsetX1 = Animatable(Constants.OFF_SET_X_1)
    val offsetY1 = Animatable(Constants.OFF_SET_Y_1)


    val offsetX2 = Animatable(Constants.OFF_SET_X_1)
    val offsetY2 = Animatable(Constants.OFF_SET_Y_2)

    val offsetX3 = Animatable(Constants.OFF_SET_X_3)
    val offsetY3 = Animatable(Constants.OFF_SET_Y_3)


    init {
        categoryWorkout()
    }

    fun onEvent(event: WorkoutUIEvent) {
        when (event) {
            is WorkoutUIEvent.RefreshData -> {
                categoryWorkout()
            }
        }
    }


    fun categoryWorkout() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _categoryWorkoutResponse.value = NetworkResult.Loading()
            repository.categoryBaseWorkout().collect { response ->
                featureWorkout()
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

    private fun featureWorkout() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _featureWorkoutResponse.value = NetworkResult.Loading()
            repository.featureWorkout().collect { response ->
                response.apply {
                    _featureWorkoutResponse.value = response
                }
            }
        } else {
            _featureWorkoutResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }


    fun resetResponse() {
        _featureWorkoutResponse.value = NetworkResult.NoCallYet()
        _categoryWorkoutResponse.value = NetworkResult.NoCallYet()
    }

}


