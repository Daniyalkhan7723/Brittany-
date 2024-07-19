package com.cp.brittany.dixon.model.workout

data class WorkoutDetailsResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val data: WorkoutData,
)
