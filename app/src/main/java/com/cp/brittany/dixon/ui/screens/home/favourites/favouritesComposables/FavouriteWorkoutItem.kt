package com.cp.brittany.dixon.ui.screens.home.favourites.favouritesComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun FavouriteWorkoutItem(
    navigateToDetailsScreen: () -> Unit,
    workoutItem: WorkoutData,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context).data(workoutItem.video?.thumbnail)
        .placeholder(R.drawable.workout_place_holder).error(R.drawable.workout_place_holder)
        .fallback(R.drawable.workout_place_holder).crossfade(true).build()

    val onClickItem by remember {
        mutableStateOf(Modifier.clickable {
            navigateToDetailsScreen()
        })
    }

    Row(
        Modifier
            .padding(top = 10.sdp)
            .then(onClickItem)
            .fillMaxWidth()
    ) {
        // Load and display the image with AsyncImage
        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 5.sdp)
                .width(152.sdp)
                .height(95.sdp)
                .clip(RoundedCornerShape(10.sdp)),
            contentScale = ContentScale.Crop,
        )
        Box(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(start = 10.sdp, top = 14.sdp, end = 10.sdp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = workoutItem.title ?: "",
                    modifier = Modifier.padding(start = 4.sdp),
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = AppFont.MyCustomFont
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(2.sdp))

                Text(
                    text = workoutItem.published_time ?: "",
                    maxLines = 2,
                    modifier = Modifier
                        .padding(start = 4.sdp),
                    style = TextStyle(
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = AppFont.MyCustomFont
                    ), color = MaterialTheme.colorScheme.secondary
                )

                Spacer(Modifier.height(2.sdp))

                Text(
                    text = workoutItem.description ?: "",
                    maxLines = 2,
                    modifier = Modifier
                        .padding(start = 4.sdp),
                    style = TextStyle(
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = AppFont.MyCustomFont
                    ), color = MaterialTheme.colorScheme.secondaryContainer
                )


            }

        }

    }
}
