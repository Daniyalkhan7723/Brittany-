package com.cp.brittany.dixon.model.favourite

import androidx.compose.runtime.Immutable
import com.cp.brittany.dixon.model.workout.WorkoutData

@Immutable
data class GetWorkoutFavourites(
    val data: WorkoutFavouriteData,
    val message: String,
    val pagination: Any,
    val status: Boolean,
    val statusCode: Int
)

@Immutable
data class WorkoutFavouriteData(
    val latest_workout: WorkoutData,
    val favourited_workout: ArrayList<WorkoutData>,

    )