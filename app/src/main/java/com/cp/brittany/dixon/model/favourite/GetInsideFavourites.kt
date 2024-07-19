package com.cp.brittany.dixon.model.favourite

import androidx.compose.runtime.Immutable
import com.cp.brittany.dixon.model.insight.AllInsight

@Immutable
data class GetInsideFavourites(
    val data: WorkoutInsideData,
    val message: String,
    val pagination: Any,
    val status: Boolean,
    val statusCode: Int
)

@Immutable
data class WorkoutInsideData(
    val latest: AllInsight,
    val insights: ArrayList<AllInsight>,

    )