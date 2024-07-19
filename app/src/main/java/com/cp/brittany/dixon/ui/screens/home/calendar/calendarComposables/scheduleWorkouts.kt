package com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.workout.WorkoutData

fun LazyListScope.scheduleWorkouts(
    workoutByDateResponseData: MutableList<WorkoutData>,
    navigateToScheduleWorkoutsDetailsScreen: (WorkoutData) -> Unit,
    modifier: Modifier = Modifier

) {
    items(count = workoutByDateResponseData.size, key = {
        workoutByDateResponseData[it].id ?: 0
    }, itemContent = { index ->
        val workouts = workoutByDateResponseData[index]

        ScheduledWorkoutItem(
            index = index,
            listSize = workoutByDateResponseData.size,
            workoutByDate = workouts,
            navigateToScheduleWorkoutsDetailsScreen = {
                navigateToScheduleWorkoutsDetailsScreen(
                    workouts
                )
            },
            modifier = modifier
        )
    })
}