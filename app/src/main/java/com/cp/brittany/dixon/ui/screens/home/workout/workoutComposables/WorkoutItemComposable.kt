package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.workout.Data
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp

@Composable
fun WorkoutItem(
    modifier: Modifier,
    navigateToDetails: (String, String) -> Unit,
    workoutItemData: Data,
    sharedViewModel: SharedViewModel,
    navigateToWorkoutSeeMore: (Data) -> Unit,
    indexValue: Int

) {
    val context = LocalContext.current

    val onNavigateToWorkoutSeeMore = remember {
        {
            navigateToWorkoutSeeMore(workoutItemData)
        }
    }



    Spacer(
        modifier =
        if (indexValue == 0) {
            Modifier.height(3.sdp)
        } else {
            Modifier.height(20.sdp)
        },
    )

    if (workoutItemData.workouts?.isEmpty() != true) {
        ViewAllWorkoutsComponent(
            modifier = modifier.padding(start = 15.sdp, end = 10.sdp),
            text = workoutItemData.name ?: "",
            showBackButton = true,
            navigateToViewAllData = onNavigateToWorkoutSeeMore
        )
    }


    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.sdp),
        modifier = Modifier.padding(top = 10.sdp, start = 15.sdp)
    ) {
        val data = workoutItemData.workouts
        items(count = data?.size ?: 0, key = {
            data?.get(it)?.id ?: 0
        }, itemContent = { index ->

            val workouts = data?.get(index)
            val imageRequest =
                ImageRequest.Builder(context).data(workouts?.video?.thumbnail)
                    .placeholder(R.drawable.workout_place_holder)
                    .error(R.drawable.workout_place_holder)
                    .fallback(R.drawable.workout_place_holder).crossfade(true).build()


            WorkoutSubItem(
                modifier = modifier
                    .width(200.sdp)
                    .height(120.sdp)
                    .clickable {
//                        sharedViewModel.shareWorkoutDataToDetailScreen(workouts)
                        navigateToDetails(
                            workouts?.category_id.toString(),
                            workouts?.id.toString()
                        )
                    },
                titleTextModifier = Modifier.padding(
                    top = 10.sdp
                ),
                workouts = workouts,
                imageRequest = imageRequest,
                screenType = Constants.WORKOUTS,
            )
        })
    }
}