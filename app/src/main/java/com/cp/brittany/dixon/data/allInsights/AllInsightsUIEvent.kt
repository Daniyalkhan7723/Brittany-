package com.cp.brittany.dixon.data.allInsights

sealed class AllInsightsUIEvent {

    data class SearchChanged(val searchQuery: String) : AllInsightsUIEvent()
}
