package com.cp.brittany.dixon.ui.viewModel.workout

import androidx.compose.runtime.Immutable
import com.cp.brittany.dixon.model.workout.WorkoutData

@Immutable
data class FeatureWorkoutModel(
    val data: WorkoutData,
    val message: String,
    val pagination: Any,
    val status: Boolean,
    val statusCode: Int
)