package com.cp.brittany.dixon.ui.screens.home.favourites.favouritesComposables

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cp.brittany.dixon.model.workout.WorkoutData

fun LazyListScope.favouriteWorkouts(
    workoutData: MutableList<WorkoutData>,
    navigateToWorkoutDetailsScreen: (WorkoutData,String) -> Unit,
    modifier: Modifier = Modifier
) {
    items(count = workoutData.size, key = {
        workoutData[it].id ?: 0
    }, itemContent = { index ->
        val insights = workoutData[index]
        var onNavigateToInsightsDetails = remember {
            {
                navigateToWorkoutDetailsScreen(workoutData[index],workoutData[index].id.toString())
            }
        }
        FavouriteWorkoutItem(
            navigateToDetailsScreen = onNavigateToInsightsDetails,
            workoutItem = insights,
        )
    })
}