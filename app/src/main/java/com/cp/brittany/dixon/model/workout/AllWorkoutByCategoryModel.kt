package com.cp.brittany.dixon.model.workout

import androidx.compose.runtime.Immutable

@Immutable
data class WorkoutByCategoryModel(
    val data: List<WorkoutData>,
    val message: String,
    val pagination: Pagination,
    val status: Boolean,
    val statusCode: Int
)

@Immutable
data class Pagination(
    val current_page: Int,
    val last_page: Int
)