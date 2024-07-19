package com.cp.brittany.dixon.ui.screens.home.calendar.calendarComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.calendar.Images
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.BorderButtonRound
import com.cp.brittany.dixon.ui.components.ClickableTextComposable
import com.cp.brittany.dixon.ui.components.DashedBorder
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@Composable
fun ScheduledWorkoutItem(
    navigateToScheduleWorkoutsDetailsScreen: () -> Unit,
    index: Int,
    listSize: Int,
    workoutByDate: WorkoutData,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context).placeholder(R.drawable.place_holder_3)
        .error(R.drawable.place_holder_3).fallback(R.drawable.place_holder_3).crossfade(true)
        .build()


    val onClickItem by remember {
        mutableStateOf(Modifier.clickable {
            navigateToScheduleWorkoutsDetailsScreen()
        })
    }

    val navigateToWorkoutDetailScreen = remember {
        {
            navigateToScheduleWorkoutsDetailsScreen()
        }

    }

    val onClickReadMore = remember<(String) -> Unit> {
        {
            navigateToScheduleWorkoutsDetailsScreen()
        }

    }


    Box(
        modifier = modifier
            .wrapContentSize()
            .then(onClickItem)
    ) {

        Box(
            modifier = Modifier
                .padding(start = 10.sdp, top = 13.sdp, bottom = 35.sdp, end = 10.sdp)
                .fillMaxWidth()
                .border(
                    width = 1.sdp,
                    color = Color(0xFF31363D),
                    shape = RoundedCornerShape(percent = 5)
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(9.sdp)
                )
        ) {
            Column {
                Row(
                    Modifier
                        .padding(top = 10.sdp, bottom = 10.sdp, start = 10.sdp)
                        .fillMaxWidth()
                ) {
                    // Load and display the image with AsyncImage
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = null,
                        modifier = Modifier
                            .width(71.sdp)
                            .height(58.sdp)
                            .clip(RoundedCornerShape(10.sdp)),
                        contentScale = ContentScale.Crop,
                    )
                    Box(Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(start = 10.sdp, top = 4.sdp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = workoutByDate.title ?: "",
                                modifier = Modifier.padding(start = 4.sdp),
                                style = TextStyle(
                                    fontSize = 13.ssp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = AppFont.MyCustomFont
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(Modifier.height(2.sdp))

                            ClickableTextComposable(modifier = Modifier.padding(start = 4.sdp),
                                description = workoutByDate.description ?: "",
                                onTextSelected = {
                                    onClickReadMore(it)
                                }
                            )
                        }

                    }

                }

                if (workoutByDate.images?.isEmpty() != true) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.sdp, end = 10.sdp)
                            .height((1.5).dp)
                            .background(Color(0xFF35393e))
                    )

                    setImages(workoutByDate.images)

                }


                BorderButtonRound(
                    value = stringResource(R.string.view_detail),
                    onButtonClicked = navigateToWorkoutDetailScreen,
                    paddingValues = PaddingValues(horizontal = 20.sdp),
                    modifier = Modifier
                        .width(270.sdp)
                        .align(CenterHorizontally)
                        .height(31.sdp),
                    shapeRadius = 8,
                    borderRadius = 10,
                    textColor = MaterialTheme.colorScheme.tertiaryContainer,
                    borderColor = MaterialTheme.colorScheme.tertiaryContainer
                )

                Spacer(Modifier.height(10.sdp))
            }
        }

        if (index != 0) {
            Image(
                painter = painterResource(id = R.drawable.ic_dot_1),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 5.sdp)
                    .size(18.sdp)
                    .align(TopCenter)
            )
        }


        if (listSize > 1 && index < listSize - 1) {
            Column(modifier = Modifier.align(BottomCenter)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_dot_1),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 5.sdp)
                        .size(18.sdp)
                        .align(CenterHorizontally)

                )
                DashedBorder(modifier = Modifier)
            }
        }


    }


}

@Composable
private fun setImages(
    images: MutableList<Images>?
) {
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.sdp)
            .padding(top = 10.sdp, bottom = 10.sdp, start = 10.sdp, end = 10.sdp)
    ) {
        items(count = images?.size ?: 0, key = {
            images?.get(it)?.id ?: 0
        }, itemContent = { index ->
            if (index <= 5) {
                val galleriesPhotos =
                    ImageRequest.Builder(context).data(images?.get(index)?.image ?: "")
                        .placeholder(R.drawable.place_holder_3).error(R.drawable.place_holder_3)
                        .fallback(R.drawable.place_holder_3).crossfade(true).build()

                Box {

                    if (index <= 4) {
                        AsyncImage(
                            model = galleriesPhotos,
                            contentDescription = null,
                            modifier = Modifier
                                .width(58.sdp)
                                .height(59.sdp)
                                .clip(RoundedCornerShape(7.sdp)),
                            contentScale = ContentScale.Crop,
                        )

                    }


                    if ((images?.size ?: 0) > 5) {
                        if (index == 4)
                            Box(modifier = with(
                                Modifier
                                    .width(60.sdp)
                                    .height(59.sdp)
                            ) {
                                fillMaxSize().paint(
                                    // Replace with your image id
                                    painterResource(id = R.drawable.transparent_layout),
                                    contentScale = ContentScale.FillBounds
                                )

                            }) {
                                val remainedImages = images?.size?.minus(5)
                                BoldTextComponent(
                                    value = "+ $remainedImages",
                                    textSize = 14.ssp,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.align(Center)
                                )
                            }
                    }

                }
            } else {
                return@items
            }
        })
    }
}
