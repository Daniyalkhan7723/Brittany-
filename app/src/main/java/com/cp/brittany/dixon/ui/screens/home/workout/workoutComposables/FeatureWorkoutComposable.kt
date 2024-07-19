package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.ItalicBoldTextComponent
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutViewModel
import com.cp.brittany.dixon.utills.getFormattedDate
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun FeatureWorkoutComposable(
    navigateToDetails: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
) {
    val click by remember {
        mutableStateOf(Modifier.clickable {
//            sharedViewModel.shareWorkoutDataToDetailScreen(workoutViewModel.getFeatureWorkoutResponseDataData.value)
            navigateToDetails(
                workoutViewModel.getFeatureWorkoutResponseData.value.category_id.toString(),
                workoutViewModel.getFeatureWorkoutResponseData.value.id.toString()
            )
        })
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.sdp))
            .then(click)
            .padding(start = 15.sdp, end = 5.sdp, top = 15.sdp, bottom = 15.sdp)
            .height(158.sdp),
    ) {
        Image(
            painter = painterResource(R.drawable.background_banner),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(R.drawable.logo_banner),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(230.sdp)
                .fillMaxWidth()
                .align(Alignment.TopEnd)

        )

        Column(
            modifier = Modifier.padding(top = 20.sdp),

            ) {

            FeatureWorkoutItems(
                value = workoutViewModel.getFeatureWorkoutResponseData.value.category ?: "",
                textSize = 11.ssp,
                textColor = MaterialTheme.colorScheme.inversePrimary,
                textModifier = Modifier
                    .padding(start = 4.sdp, end = 7.sdp, top = 5.sdp, bottom = 5.sdp)

            )

            FeatureWorkoutItems(
//                value = workoutViewModel.getFeatureWorkoutResponseData.value.title ?: "",
                value = workoutViewModel.getFeatureWorkoutResponseData.value.title ?: "",
                textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.onSecondary,
                textModifier = Modifier
                    .padding(start = 4.sdp, end = 7.sdp, top = 5.sdp, bottom = 5.sdp),
                modifier = Modifier.padding(end = 10.sdp)
            )

            FeatureWorkoutItems(
                value = "${workoutViewModel.getFeatureWorkoutResponseData.value.video?.duration?.getFormattedDate()} CIRCUIT",
                textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.onBackground,
                textModifier = Modifier
                    .padding(start = 4.sdp, end = 7.sdp, top = 5.sdp, bottom = 5.sdp),
                backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant

            )


            Row(
                modifier = Modifier
                    .padding(start = 5.sdp, top = 10.sdp)
                    .wrapContentSize()
            ) {
                Image(
                    modifier = Modifier
                        .size(15.sdp)
                        .align(CenterVertically),
                    painter = painterResource(id = R.drawable.ic_play_banner),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                ItalicBoldTextComponent(
                    value = stringResource(id = R.string.start_for_free),
                    textSize = 10.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 4.sdp)
                        .align(CenterVertically)
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.sdp)
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(16.sdp))
                        .background(MaterialTheme.colorScheme.onSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    HeadingTextComponentWithoutFullWidth(
                        value = stringResource(id = R.string.featured_workout),
                        textSize = 8.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 5.sdp, horizontal = 14.sdp)
                    )
                }
            }


        }

    }
}