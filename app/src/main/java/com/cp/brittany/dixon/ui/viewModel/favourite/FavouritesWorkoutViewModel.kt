package com.cp.brittany.dixon.ui.viewModel.favourite

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.favourites.FavouriteUIState
import com.cp.brittany.dixon.data.favourites.FavouritesUiEvent
import com.cp.brittany.dixon.model.favourite.AddFavourite
import com.cp.brittany.dixon.model.favourite.GetWorkoutFavourites
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
import javax.inject.Inject

@HiltViewModel
class FavouritesWorkoutViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {
    private val _favouriteWorkoutResponse: MutableStateFlow<NetworkResult<GetWorkoutFavourites>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val favouriteWorkoutResponse: StateFlow<NetworkResult<GetWorkoutFavourites>> =
        _favouriteWorkoutResponse

    private val _addToFavouriteResponse: MutableStateFlow<NetworkResult<AddFavourite>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addToFavouriteResponse: StateFlow<NetworkResult<AddFavourite>> = _addToFavouriteResponse

    var loaderState by mutableStateOf(false)
    var getFavouriteWorkouts = mutableListOf<WorkoutData>()
    var latestWorkout = mutableStateOf(WorkoutData())
    private var favouriteUIState = mutableStateOf(FavouriteUIState())

    var showNoDataFoundOrNot by mutableStateOf(false)

    init {
        getFavouriteWorkouts()
    }

    fun onEvent(event: FavouritesUiEvent) {
        when (event) {
            is FavouritesUiEvent.AddToFavourites -> {
                favouriteUIState.value = favouriteUIState.value.copy(
                    id = event.id
                )
                addToFavourite()
            }

            is FavouritesUiEvent.GetFavouriteData -> {
                getFavouriteWorkouts()
            }

        }
    }

    private fun getFavouriteWorkouts() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _favouriteWorkoutResponse.value = NetworkResult.Loading()
            repository.workoutFavouriteList().collect { response ->
                response.apply {
                    _favouriteWorkoutResponse.value = response
                }
            }
        } else {
            _favouriteWorkoutResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    private fun addToFavourite() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _addToFavouriteResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("workout_id", favouriteUIState.value.id)
            repository.addFavourite(jsonObject)
                .collect { response ->
                    response.apply {
                        _addToFavouriteResponse.value = response
                    }
                }
        } else {
            _addToFavouriteResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }

    fun resetResponse() {
        _favouriteWorkoutResponse.value = NetworkResult.NoCallYet()
        _addToFavouriteResponse.value = NetworkResult.NoCallYet()
    }

}


