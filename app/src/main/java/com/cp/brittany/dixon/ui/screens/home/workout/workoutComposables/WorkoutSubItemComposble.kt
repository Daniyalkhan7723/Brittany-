package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.showProgress
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun WorkoutSubItem(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    titleTextModifier: Modifier = Modifier,
    likeDislikeModifier: Modifier = Modifier,
    workouts: WorkoutData?,
    imageRequest: ImageRequest,
    screenType: String
) {
    Column {
        Card(
            shape = RoundedCornerShape(10.sdp),
            elevation = CardDefaults.cardElevation(5.sdp),
            modifier = modifier
        ) {
            Box {
                AsyncImage(
                    modifier = imageModifier.fillMaxSize(),
                    model = imageRequest,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.sdp, vertical = 8.sdp)
                            .align(Alignment.BottomStart)
                            .clip(RoundedCornerShape(16.sdp))
                            .background(MaterialTheme.colorScheme.onBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        HeadingTextComponentWithoutFullWidth(
                            value = workouts?.published_time ?: "",
                            textSize = 8.ssp,
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

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = titleTextModifier
                .fillMaxWidth()
        ) {

            Text(
                text = workouts?.title ?: "",
                modifier = Modifier.width(200.sdp),
                style = TextStyle(
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = AppFont.MyCustomFont
                ),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,

                )
//            HeadingTextComponentWithoutFullWidth(
//                value = workouts?.title ?: "",
//                textSize = 12.ssp,
//                textColor = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.align(CenterVertically)
//            )


            if (screenType == Constants.FAVOURITE) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star_selected),
                    contentDescription = null,
                    modifier = likeDislikeModifier
                        .padding(end = 5.sdp)
                        .align(CenterVertically)
                        .size(22.sdp),
                )
            }

        }


    }

}