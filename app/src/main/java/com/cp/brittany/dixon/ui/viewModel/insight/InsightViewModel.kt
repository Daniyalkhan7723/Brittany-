package com.cp.brittany.dixon.ui.viewModel.insight

import android.app.Application
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.data.insights.InsightsUIEvent
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.insight.InsightModel
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
class InsightViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {
    private val _insightResponse: MutableStateFlow<NetworkResult<InsightModel>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val insightResponse: StateFlow<NetworkResult<InsightModel>> =
        _insightResponse

    var getLatestInsights = mutableListOf<AllInsight>()
    var getAllInsights = mutableListOf<AllInsight>()

    var visibleInsightItems by mutableStateOf(false)

    var workoutLoaderState by mutableStateOf(false)


    val offsetX1 = Animatable(Constants.OFF_SET_X_1)
    val offsetY1 = Animatable(Constants.OFF_SET_Y_1)

    val offsetX2 = Animatable(Constants.OFF_SET_X_1)
    val offsetY2 = Animatable(Constants.OFF_SET_Y_2)

    val offsetX3 = Animatable(Constants.OFF_SET_X_3)
    val offsetY3 = Animatable(Constants.OFF_SET_Y_3)


    init {
        getInsightsData()
    }

    fun onEvent(event: InsightsUIEvent) {
        when (event) {
            is InsightsUIEvent.Refresh -> {
                getInsightsData()
            }

        }
    }


    fun getInsightsData() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("limit", 10)
            _insightResponse.value = NetworkResult.Loading()
            repository.insightData(jsonObject).collect { response ->
                response.apply {
                    _insightResponse.value = response
                }
            }
        } else {
            _insightResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    fun resetResponse() {
        _insightResponse.value = NetworkResult.NoCallYet()
    }

}


