package com.cp.brittany.dixon.ui.viewModel.favourite

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.favourites.FavouriteUIState
import com.cp.brittany.dixon.data.favourites.FavouritesUiEvent
import com.cp.brittany.dixon.model.favourite.GetInsideFavourites
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.insight.InsightLikeModel
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
class FavouritesInsightViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {
    private val _addFavouriteResponse: MutableStateFlow<NetworkResult<InsightLikeModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addFavouriteResponse: StateFlow<NetworkResult<InsightLikeModel>> = _addFavouriteResponse

    private val _favouriteInsideResponse: MutableStateFlow<NetworkResult<GetInsideFavourites>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val favouriteInsideResponse: StateFlow<NetworkResult<GetInsideFavourites>> =
        _favouriteInsideResponse

    var showNoDataFoundOrNot by mutableStateOf(false)

    var loaderState by mutableStateOf(false)

    var getFavouriteInsight = mutableListOf<AllInsight>()
    var latestInsight = mutableStateOf(AllInsight())
    private var favouriteUIState = mutableStateOf(FavouriteUIState())

    init {
        getFavouriteInsight()
    }

    fun onEvent(event: FavouritesUiEvent) {
        when (event) {
            is FavouritesUiEvent.AddToFavourites -> {
                favouriteUIState.value = favouriteUIState.value.copy(
                    id = event.id
                )
                addFavourite()
            }

            is FavouritesUiEvent.GetFavouriteData -> {
                getFavouriteInsight()
            }

        }
    }

    private fun addFavourite() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _addFavouriteResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("insight_id", favouriteUIState.value.id)
            repository.addInsightFavourite(jsonObject)
                .collect { response ->
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


    private fun getFavouriteInsight() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _favouriteInsideResponse.value = NetworkResult.Loading()
            repository.insideFavouriteList().collect { response ->
                response.apply {
                    _favouriteInsideResponse.value = response
                }
            }
        } else {
            _favouriteInsideResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    fun resetResponse() {
        _favouriteInsideResponse.value = NetworkResult.NoCallYet()
        _addFavouriteResponse.value = NetworkResult.NoCallYet()
    }

}


