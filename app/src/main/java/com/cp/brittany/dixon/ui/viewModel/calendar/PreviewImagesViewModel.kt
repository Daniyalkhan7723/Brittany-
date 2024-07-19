package com.cp.brittany.dixon.ui.viewModel.calendar

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.DeleteImageUIEvent
import com.cp.brittany.dixon.data.calendar.DeleteImageUIState
import com.cp.brittany.dixon.model.calendar.DeleteImageResponse
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
class PreviewImagesViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationContext: Application,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle,
    val dataSource: CalendarDataSource = CalendarDataSource()
) : ViewModel() {
    private val _deleteImagesResponse: MutableStateFlow<NetworkResult<DeleteImageResponse>> =
        MutableStateFlow(NetworkResult.NoCallYet())
    val deleteImagesResponse: StateFlow<NetworkResult<DeleteImageResponse>> =
        _deleteImagesResponse

    var deleteImagesApiHit = mutableStateOf(false)

    private var deleteImageUIState = mutableStateOf(DeleteImageUIState())

    fun onEvent(event: DeleteImageUIEvent) {
        when (event) {
            is DeleteImageUIEvent.DeleteImage -> {
                deleteImageUIState.value = deleteImageUIState.value.copy(
                    imageId = event.imageId
                )
                deleteImagesApiHit.value = true
                deleteImages()
            }
        }
    }

    private fun deleteImages() = viewModelScope.launch(Dispatchers.IO) {
        if (applicationContext.isNetworkAvailable()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("id", deleteImageUIState.value.imageId.toInt())
            _deleteImagesResponse.value = NetworkResult.Loading()
            repository.deleteImage(jsonObject).collect { response ->
                response.apply {
                    _deleteImagesResponse.value = response
                }
            }
        } else {
            _deleteImagesResponse.value = NetworkResult.NoInternet(
                applicationContext.resources.getString(
                    R.string.no_internet
                )
            )
        }
    }

    fun resetResponse() {
        _deleteImagesResponse.value = NetworkResult.NoCallYet()
    }

}


