package com.cp.brittany.dixon.data.allWorkouts

sealed class AllWorkoutsUIEvent {

    data class SearchChanged(val searchQuery: String) : AllWorkoutsUIEvent()
}
