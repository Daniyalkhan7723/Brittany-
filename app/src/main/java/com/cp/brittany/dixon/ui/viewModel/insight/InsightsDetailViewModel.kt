package com.cp.brittany.dixon.ui.viewModel.insight

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.allInsights.AllInsightsUIState
import com.cp.brittany.dixon.data.allInsights.InsightDetailsUIEvent
import com.cp.brittany.dixon.model.insight.InsightLikeModel
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.ui.repository.Repository
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
class InsightsDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,

    ) : ViewModel() {

    private val _addFavouriteResponse: MutableStateFlow<NetworkResult<InsightLikeModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val addFavouriteResponse: StateFlow<NetworkResult<InsightLikeModel>> = _addFavouriteResponse

    private val _likeResponse: MutableStateFlow<NetworkResult<InsightLikeModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val likeResponse: StateFlow<NetworkResult<InsightLikeModel>> = _likeResponse


    var getScheduleWorkoutsDetailData = mutableStateOf(false)
    var insightUIState = mutableStateOf(AllInsightsUIState())

    var favouriteImageState =
        mutableStateOf(false)

    var likeState =
        mutableStateOf(0)

    init {
        getScheduleWorkoutsDetailData.value = true
    }


    fun onEvent(insight: InsightDetailsUIEvent) {
        when (insight) {
            is InsightDetailsUIEvent.AddFavouriteChanged -> {
                insightUIState.value = insightUIState.value.copy(
                    insightId = insight.id
                )
                addFavourite()
            }

            is InsightDetailsUIEvent.LikeChanged -> {
                insightUIState.value = insightUIState.value.copy(
                    insightId = insight.id
                )
                like()
            }
        }
    }

    private fun addFavourite() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _addFavouriteResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("insight_id", insightUIState.value.insightId ?: 0)
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

    private fun like() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            _likeResponse.value = NetworkResult.Loading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("insight_id", insightUIState.value.insightId ?: 0)
            repository.likeInsight(jsonObject)
                .collect { response ->
                    response.apply {
                        _likeResponse.value = response
                    }
                }
        } else {
            _likeResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }

    }

    fun resetResponse() {
        _addFavouriteResponse.value = NetworkResult.NoCallYet()
        _likeResponse.value = NetworkResult.NoCallYet()
    }

}


