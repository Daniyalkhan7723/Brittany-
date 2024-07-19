package com.cp.brittany.dixon.data.workout

sealed class WorkoutDetailUIEvent {
    data class AddFavouriteChanged(val id: Int?) : WorkoutDetailUIEvent()
    data class AddScheduleChanged(val dates: String?, val id: Int?) : WorkoutDetailUIEvent()
    object RefreshDetails : WorkoutDetailUIEvent()
    data class WorkoutStart(val workoutId: Int?, val workoutVideoId: Int?) :
        WorkoutDetailUIEvent()
}
