package com.cp.brittany.dixon.data.allInsights

sealed class InsightDetailsUIEvent {
    data class AddFavouriteChanged(val id: Int?) : InsightDetailsUIEvent()
    data class LikeChanged(val id: Int?) : InsightDetailsUIEvent()
}
