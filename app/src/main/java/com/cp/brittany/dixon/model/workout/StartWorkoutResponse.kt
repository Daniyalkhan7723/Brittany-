package com.cp.brittany.dixon.model.workout

data class StartWorkoutResponse(
    val data: StartWorkoutResponseData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class StartWorkoutResponseData(
    val created_at: String,
    val id: Int,
    val is_played: Boolean,
    val updated_at: String,
    val user_id: Int,
    val workout_id: Int,
    val workout_video_id: Int
)