package com.cp.brittany.dixon.ui.viewModel.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cp.brittany.dixon.data.allWorkouts.AllWorkoutsUIEvent
import com.cp.brittany.dixon.data.allWorkouts.AllWorkoutsUIState
import com.cp.brittany.dixon.ui.repository.Repository
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class WorkoutAllSearchViewModel @Inject constructor(
    private val repository: Repository,
    val preference: SharePreferenceHelper,
    val stateHandle: SavedStateHandle

) : ViewModel() {
    var loaderState = mutableStateOf(false)
    var allWorkoutsUIState = mutableStateOf(AllWorkoutsUIState())

    var isSearchVisible by mutableStateOf(false)

    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "",
    )


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val allWorkouts = search.debounce(300.milliseconds).flatMapLatest { query ->
        repository.allWorkouts(
            token = preference.getToken() ?: "",
            categoryId = stateHandle.get<String>(Constants.CATEGORY_ID) ?: "",
            searchItem = query,
            screenType = stateHandle.get<String>(Constants.SCREEN_TYPE) ?: ""
        ).cachedIn(viewModelScope)
    }


    fun setSearch(query: String) {
        _search.value = query
    }


    fun onEvent(event: AllWorkoutsUIEvent) {
        when (event) {
            is AllWorkoutsUIEvent.SearchChanged -> {
                allWorkoutsUIState.value = allWorkoutsUIState.value.copy(
                    searchQuery = event.searchQuery
                )
            }
        }
    }


}


