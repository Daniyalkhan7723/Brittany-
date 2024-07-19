package com.cp.brittany.dixon.ui.screens.home.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.calendar.ScheduleWorkoutDetailViewModel
import com.cp.brittany.dixon.utills.getFormattedDate
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp


@Composable
fun ScheduledWorkoutDetailScreen(
    onBackPress: () -> Unit,
    onNavigateToImagePreviousScreen: (String, String) -> Unit,
    sharedViewModel: SharedViewModel,
    workoutViewDetailModel: ScheduleWorkoutDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val hitWhenImageIsDeleted by sharedViewModel.refreshWorkoutImagesWhenDeleteSingleImage.collectAsStateWithLifecycle()


    val navigateToWorkoutSeeMore = remember {
        {

        }
    }


    val backPressClick by remember {
        mutableStateOf(Modifier.clickable {
            onBackPress()
        })
    }


    workoutViewDetailModel.apply {

        if (getScheduleWorkoutsDetailData.value) {
            getScheduleWorkoutsDetailData.value = false

            sharedViewModel.shareWorkoutDataToDetailScreenResponse.value?.let {
                imagesList.value = it
            }
        }

        LaunchedEffect(key1 = Unit) {
            if (hitWhenImageIsDeleted.deleteOrNot) {
                sharedViewModel.refreshDetailScreen(true)
                deleteImage(hitWhenImageIsDeleted.imageId)
                sharedViewModel.resetResponse()
            }
        }


    }

    val imageRequest = ImageRequest.Builder(context)
        .data(sharedViewModel.shareWorkoutDataToDetailScreenResponse.value?.video?.thumbnail ?: "")
        .placeholder(R.drawable.workout_place_holder).error(R.drawable.workout_place_holder)
        .fallback(R.drawable.workout_place_holder).crossfade(true).build()


    BrittanyDixonTheme {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.sdp),
                    model = imageRequest,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Card(
                    shape = RoundedCornerShape(topStart = 15.sdp, topEnd = 15.sdp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    onClick = {

                    },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .height(900.sdp)
                        .padding(top = 290.sdp)
                ) {
                    Column(modifier = Modifier.padding(start = 15.sdp, end = 15.sdp)) {
                        val time =
                            "${sharedViewModel.shareWorkoutDataToDetailScreenResponse.collectAsStateWithLifecycle().value?.video?.duration?.getFormattedDate()}"

                        Row(modifier = Modifier.padding(top = 15.sdp)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.size(15.sdp)
                            )

                            Text(
                                text = time,
                                modifier = Modifier.padding(start = 5.sdp),
                                style = TextStyle(
                                    fontSize = 12.ssp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = AppFont.MyCustomFont
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )

                        }
                        BoldTextComponent(
                            value = sharedViewModel.shareWorkoutDataToDetailScreenResponse.value?.title
                                ?: "",
                            textSize = 16.ssp,
                            textColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 10.sdp)
                        )

                        Spacer(modifier = Modifier.height(2.sdp))

                        NormalTextComponentWithoutFullWidth(
                            value = sharedViewModel.shareWorkoutDataToDetailScreenResponse.value?.description
                                ?: "",
                            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            textSize = 12.ssp,
                            fontStyle = FontStyle.Normal,
                            modifier = Modifier
                                .heightIn(min = 40.sdp)
                                .padding(top = 10.sdp),
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(top = 5.sdp)
                            .height((2).dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )

                    Spacer(
                        modifier = Modifier.height(15.sdp)
                    )

                    ViewAllWorkoutsComponent(
                        modifier = Modifier.padding(start = 12.sdp, end = 12.sdp),
                        text = stringResource(R.string.workout_photos),
                        showBackButton = false,
                        navigateToViewAllData = navigateToWorkoutSeeMore
                    )

                    Spacer(
                        modifier = Modifier.height(10.sdp)
                    )

                    GirdView(
                        modifier = Modifier.heightIn(max = 1000.dp),
                        onNavigateToImagePreviewScreen = onNavigateToImagePreviousScreen
                    )

                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 40.sdp, start = 10.sdp)
                    .then(backPressClick)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .rotate(180f)
                        .size(18.sdp),

                    )

                NormalTextComponentWithoutFullWidth(
                    value = stringResource(id = R.string.workouts),
                    textSize = 14.ssp,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )

            }


        }
    }
}

@Composable
private fun GirdView(
    workoutViewDetailModel: ScheduleWorkoutDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToImagePreviewScreen: (String, String) -> Unit,
) {
    val context = LocalContext.current
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 12.sdp, end = 12.sdp, bottom = 16.sdp
        ),
        content = {
            items(count = workoutViewDetailModel.imagesList.value.images?.size ?: 0, key = {
                workoutViewDetailModel.imagesList.value.images?.get(it)?.id ?: 0
            }, itemContent = { index ->
                val galleriesPhotos = ImageRequest.Builder(context)
                    .data(workoutViewDetailModel.imagesList.value.images?.get(index)?.image ?: "")
                    .placeholder(R.drawable.place_holder_3).error(R.drawable.place_holder_3)
                    .fallback(R.drawable.place_holder_3).crossfade(true).build()


                AsyncImage(model = galleriesPhotos,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.sdp, bottom = 5.sdp)
                        .clickable {
                            onNavigateToImagePreviewScreen(
                                workoutViewDetailModel.imagesList.value.images?.get(index)?.image
                                    ?: "",
                                workoutViewDetailModel.imagesList.value.images?.get(index)?.id.toString()
                            )
                        }
                        .size(110.sdp),
                    contentScale = ContentScale.Crop)
            })
        })
}