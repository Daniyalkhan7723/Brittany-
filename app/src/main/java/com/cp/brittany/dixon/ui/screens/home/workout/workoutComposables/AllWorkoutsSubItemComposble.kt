package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.showProgress
import com.cp.brittany.dixon.ui.theme.darkGrey
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun AllWorkoutsSubItem(
    modifier: Modifier = Modifier,
    workouts: WorkoutData?,
    imageRequest: ImageRequest,
    sharedViewModel: SharedViewModel,
    navigateToDetails: (String, String) -> Unit,

    ) {
    val context = LocalContext.current

    val click by remember {
        mutableStateOf(Modifier.clickable {
//            sharedViewModel.shareWorkoutDataToDetailScreen(workouts)
            navigateToDetails(workouts?.category_id.toString(), workouts?.id.toString())
        })
    }

    Box(
        modifier = Modifier
            .then(click)
            .padding(bottom = 15.sdp, start = 10.sdp, end = 10.sdp)
            .border(
                width = 1.sdp,
                color = Color(0xFF31363D),
                shape = RoundedCornerShape(percent = 5)
            )
            .background(
                color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(9.sdp)
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(
                start = 8.sdp, end = 8.sdp, top = 6.sdp, bottom = 10.sdp
            )
        ) {
            Card(
                shape = RoundedCornerShape(10.sdp),
                colors = CardDefaults.cardColors(
                    containerColor = darkGrey, //Card background color
                ),
                elevation = CardDefaults.cardElevation(5.sdp),
                modifier = modifier
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier.height(170.sdp),
                        model = imageRequest,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 10.sdp, vertical = 13.sdp)
                                .align(Alignment.BottomStart)
                                .clip(RoundedCornerShape(16.sdp))
                                .background(MaterialTheme.colorScheme.onBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            HeadingTextComponentWithoutFullWidth(
                                value = workouts?.published_time ?: "",
                                textSize = 10.ssp,
                                textColor = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 5.sdp, horizontal = 14.sdp)
                            )

                        }
                    }

                    if ((workouts?.progress ?: -1) > 0) {
                        showProgress(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.sdp)
                                .align(Alignment.BottomStart)
                                .padding(start = 1.sdp)
                                .clip(
                                    RoundedCornerShape(
                                        bottomStartPercent = 100
                                    )
                                )
                                .background(Color.Transparent),
                            progress = (workouts?.progress)?.times(2) ?: 0
                        )
                    }

                }

            }

            HeadingTextComponentWithoutFullWidth(
                value = workouts?.title ?: "",
                textSize = 12.ssp,
                textColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 7.sdp),
            )

        }
    }


//    Card(
//        shape = RoundedCornerShape(8.sdp),
//        colors = CardDefaults.cardColors(
//            containerColor = darkGrey, //Card background color
//        ),
//        elevation = CardDefaults.cardElevation(5.sdp),
//        modifier = Modifier
//            .padding(bottom = 15.sdp, start = 10.sdp, end = 10.sdp)
//            .then(click),
//    ) {
//        Column(
//            modifier = Modifier.padding(
//                start = 8.sdp, end = 8.sdp, top = 6.sdp, bottom = 10.sdp
//            )
//        ) {
//            Card(
//                shape = RoundedCornerShape(10.sdp),
//                colors = CardDefaults.cardColors(
//                    containerColor = darkGrey, //Card background color
//                ),
//                elevation = CardDefaults.cardElevation(5.sdp),
//                modifier = modifier
//            ) {
//                Box {
//                    AsyncImage(
//                        modifier = Modifier.height(170.sdp),
//                        model = imageRequest,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                    )
//                    Box(modifier = Modifier.fillMaxWidth()) {
//                        Box(
//                            modifier = Modifier
//                                .wrapContentSize()
//                                .padding(horizontal = 10.sdp, vertical = 13.sdp)
//                                .align(Alignment.BottomStart)
//                                .clip(RoundedCornerShape(16.sdp))
//                                .background(MaterialTheme.colorScheme.onBackground),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            HeadingTextComponentWithoutFullWidth(
//                                value = workouts?.published_time ?: "",
//                                textSize = 10.ssp,
//                                textColor = MaterialTheme.colorScheme.surface,
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .padding(vertical = 5.sdp, horizontal = 14.sdp)
//                            )
//
//                        }
//                    }
//
//                    if ((workouts?.progress ?: -1) > 0) {
//                        showProgress(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(5.sdp)
//                                .align(Alignment.BottomStart)
//                                .padding(start = 1.sdp)
//                                .clip(
//                                    RoundedCornerShape(
//                                        bottomStartPercent = 100
//                                    )
//                                )
//                                .background(Color.Transparent),
//                            progress = (workouts?.progress)?.times(2) ?: 0
//                        )
//                    }
//
//                }
//
//            }
//
//            HeadingTextComponentWithoutFullWidth(
//                value = workouts?.title ?: "",
//                textSize = 12.ssp,
//                textColor = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(top = 7.sdp),
//            )
//
//        }
//    }


}