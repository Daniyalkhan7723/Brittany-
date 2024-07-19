package com.cp.brittany.dixon.ui.screens.home.workout

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.cp.brittany.dixon.data.workout.WorkoutDetailUIEvent
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.ButtonComponentNoFullWidth
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.calendar.DateCalendarBottomSheet
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.CustomArcShape
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutSubItem
import com.cp.brittany.dixon.ui.theme.AppFont
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutDetailViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.getFormattedDateWithSec
import com.cp.brittany.dixon.utills.sheets.rememberBottomSheetState
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun WorkoutDetailScreen(
    onBackPress: () -> Unit,
    sharedViewModel: SharedViewModel,
    workoutViewDetailModel: WorkoutDetailViewModel = hiltViewModel(),
    navigateToVideoPlayer: (String, String, String, String, Int) -> Unit,
    screenType: String,
    navigateToWorkoutDetails: (WorkoutData, String) -> Unit,

    ) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val categoryViewDetailResponse by workoutViewDetailModel.categoryWorkoutResponse.collectAsStateWithLifecycle()
    val workoutDetailResponse by workoutViewDetailModel.workoutDetail.collectAsStateWithLifecycle()
    val addToFavouriteResponse by workoutViewDetailModel.addFavouriteResponse.collectAsStateWithLifecycle()
    val startWorkoutResponse by workoutViewDetailModel.startWorkoutResponse.collectAsStateWithLifecycle()
    val addScheduleResponse by workoutViewDetailModel.addScheduleResponse.collectAsStateWithLifecycle()
    val sharedViewModelResponse by sharedViewModel.navigateBackFromVideoPlayerScreen.collectAsStateWithLifecycle()
    val refreshDetailsResponse by sharedViewModel.refreshWorkoutDetails.collectAsStateWithLifecycle()
//    val detailData by sharedViewModel.shareWorkoutDataToDetailScreenResponse.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState()

    var playerBackPressState by remember {
        mutableStateOf(false)
    }

    val navigateToWorkoutSeeMore = remember {
        {

        }
    }

    var moreWorkoutsLoaderState by remember {
        mutableStateOf(false)
    }

    var getWorkoutsResponse by remember {
        mutableStateOf(false)
    }

    val backPressClick by remember {
        mutableStateOf(Modifier.clickable {
            onBackPress()
        })
    }


    val selectedDates = remember<(String) -> Unit> {
        { date ->
            if (date != "") {
                workoutViewDetailModel.onEvent(
                    WorkoutDetailUIEvent.AddScheduleChanged(
                        date, workoutViewDetailModel.workoutDetailObject.id
                    )
                )
                scope.launch { bottomSheetState.collapse() }

            } else {
                Toast.makeText(context, "Please select any date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val onNavigateToVideoPlayer = remember {
        {
//            if (workoutViewDetailModel.preference.getUser()?.is_premium == true || workoutViewDetailModel.isFeaturedWorkout) {
//                navigateToVideoPlayer(
//                    workoutViewDetailModel.workoutDetailObject.video?.url
//                        ?: "", Constants.WORKOUT_PLAYER_SCREEN
//                )
//            } else {
//                navigateToVideoPlayer(
//                    "", Constants.SUBSCRIPTION_SCREEN
//                )
//            }

            workoutViewDetailModel.onEvent(
                WorkoutDetailUIEvent.WorkoutStart(
                    workoutViewDetailModel.workoutDetailObject.id,
                    workoutViewDetailModel.workoutDetailObject.video?.id

                )
            )
        }

    }

    val calendarClick by remember {
        mutableStateOf(Modifier.clickable {
            scope.launch { bottomSheetState.expand() }
        })
    }

    LaunchedEffect(key1 = Unit) {
        when (sharedViewModelResponse) {
            true -> {
                playerBackPressState = true
                delay(700)
                playerBackPressState = false
                sharedViewModel.resetResponse()
            }

            false -> {

            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        when (refreshDetailsResponse) {
            true -> {
                workoutViewDetailModel.onEvent(
                    WorkoutDetailUIEvent.RefreshDetails
                )

                sharedViewModel.resetRefreshDetails()
            }

            false -> {

            }
        }
    }

    workoutViewDetailModel.apply {
        when (workoutDetailResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (workoutDetailResponse.data?.status == true) {
                    isFeaturedWorkout = workoutDetailResponse.data?.data?.is_featured ?: false
                    favouriteImageState =
                        workoutDetailResponse.data?.data?.is_favorite ?: false
                    workoutDetailResponse.data?.data?.let {
                        workoutDetailObject = it
                    }

                } else {
                    showToast(
                        title = workoutDetailResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(workoutDetailResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    addScheduleResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                loaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                loaderState = false
                resetResponse()
                showToast(
                    title = workoutDetailResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        if (handleStartWorkoutRecomposition) {
            when (startWorkoutResponse) {
                is NetworkResult.Success<*> -> {
                    handleStartWorkoutRecomposition = false
                    loaderState = false
                    if (startWorkoutResponse.data?.status == true) {
                        navigateToVideoPlayer(
                            workoutViewDetailModel.workoutDetailObject.video?.url
                                ?: "", Constants.WORKOUT_PLAYER_SCREEN,
                            workoutViewDetailModel.workoutDetailObject.id.toString(),
                            workoutViewDetailModel.workoutDetailObject.watched_time ?: "",
                            workoutViewDetailModel.workoutDetailObject.progress ?: 0
                        )
                    } else {
                        showToast(
                            title = startWorkoutResponse.data?.message ?: "", isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    resetResponse()
                    loaderState = false
                    val message: String = try {
                        val jObjError = JSONObject(startWorkoutResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        addScheduleResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    loaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    loaderState = false
                    resetResponse()
                    showToast(
                        title = startWorkoutResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }


        if (getWorkoutsResponse) {
            when (categoryViewDetailResponse) {
                is NetworkResult.Success<*> -> {
                    getWorkoutsResponse = false
                    moreWorkoutsLoaderState = false
                    if (categoryViewDetailResponse.data?.status == true) {
                        categoryWorkoutResponseData.clear()
                        categoryViewDetailResponse.data?.data?.let {
                            categoryWorkoutResponseData.addAll(it)
                        }
                    } else {
                        showToast(
                            title = categoryViewDetailResponse.data?.message ?: "",
                            isSuccess = false
                        )
                    }
                    resetResponse()
                }

                is NetworkResult.Error<*> -> {
                    getWorkoutsResponse = false
                    moreWorkoutsLoaderState = false
                    resetResponse()
                    val message: String = try {
                        val jObjError = JSONObject(categoryViewDetailResponse.message.toString())
                        jObjError.get("message").toString()
                    } catch (e: Exception) {
                        categoryViewDetailResponse.message ?: context.resources.getString(
                            R.string.something_went_wrong
                        )
                    }

                    showToast(
                        title = message, isSuccess = false
                    )
                }

                is NetworkResult.Loading<*> -> {
                    moreWorkoutsLoaderState = true
                    resetResponse()
                }

                is NetworkResult.NoInternet<*> -> {
                    moreWorkoutsLoaderState = false
                    getWorkoutsResponse = false
                    resetResponse()
                    showToast(
                        title = categoryViewDetailResponse.message ?: "", isSuccess = false
                    )

                }

                is NetworkResult.NoCallYet<*> -> {

                }
            }
        }

        when (addToFavouriteResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (addToFavouriteResponse.data?.status == true) {
                    addToFavouriteResponse.data?.data?.is_favorite?.let {
                        favouriteImageState = it
                    }
                    sharedViewModel.refreshWorkoutListAfterLikeOrDislike(true)
                    sharedViewModel.refreshFavouriteWorkoutListAfterLikeOrDislike(true)
                    showToast(
                        title = addToFavouriteResponse.data?.message ?: "", isSuccess = true
                    )
                } else {
                    showToast(
                        title = addToFavouriteResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(addToFavouriteResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    addToFavouriteResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                loaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                loaderState = false
                resetResponse()
                showToast(
                    title = addToFavouriteResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }


        when (addScheduleResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (addScheduleResponse.data?.status == true) {
                    sharedViewModel.refreshScheduleWorkout(true)
                    showToast(
                        title = addScheduleResponse.data?.message ?: "", isSuccess = true
                    )
                } else {
                    showToast(
                        title = addScheduleResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(addScheduleResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    addScheduleResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                loaderState = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                loaderState = false
                resetResponse()
                showToast(
                    title = addScheduleResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        LaunchedEffect(key1 = Unit) {
            getWorkoutsResponse = true
        }


    }

//    DisposableEffect(lifecycleOwner) {
//        onDispose {
//            if (workoutViewDetailModel.socketIO.isConnected()!!) {
//                workoutViewDetailModel.socketIO.disconnect()
//            }
//        }
//    }

    val imageRequest = ImageRequest.Builder(context)
        .data(workoutViewDetailModel.workoutDetailObject.video?.thumbnail ?: "")
        .placeholder(R.drawable.workout_place_holder).error(R.drawable.workout_place_holder)
        .fallback(R.drawable.workout_place_holder).crossfade(true).build()

    BrittanyDixonTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(state = scrollState)
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
                    CustomArcShape(
                        screenType = screenType,
                        modifier = Modifier
                            .padding(top = 287.sdp)
                            .fillMaxSize(),
                        navigateToVideoPlayer = onNavigateToVideoPlayer

                    ) {
                        Column(modifier = Modifier.padding(start = 15.sdp, end = 15.sdp)) {

                            val time =
                                "${workoutViewDetailModel.workoutDetailObject.video?.duration?.getFormattedDateWithSec()}"

                            Row {

                                if (time != "null") {
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

                            }

                            Text(
                                text = workoutViewDetailModel.workoutDetailObject.title
                                    ?: "N/A",
                                maxLines = 1,
                                modifier = Modifier.padding(top = 10.sdp),
                                style = TextStyle(
                                    fontSize = 16.ssp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = AppFont.MyCustomFont
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                            )



                            Spacer(modifier = Modifier.height(2.sdp))

                            NormalTextComponentWithoutFullWidth(
                                value = workoutViewDetailModel.workoutDetailObject.description
                                    ?: "N/A",
                                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                textSize = 12.ssp,
                                fontStyle = FontStyle.Normal,
                                modifier = Modifier
                                    .heightIn(min = 40.sdp)
                                    .padding(top = 10.sdp),
                            )

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(vertical = 13.sdp)
                                        .align(BottomStart)
                                        .clip(RoundedCornerShape(16.sdp))
                                        .background(MaterialTheme.colorScheme.onPrimary),
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_share),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(22.sdp)
                                            .padding(start = 10.sdp)
                                            .align(CenterVertically)
                                    )
                                    HeadingTextComponentWithoutFullWidth(
                                        value = stringResource(R.string.share),
                                        textSize = 10.ssp,
                                        textColor = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(
                                                top = 7.sdp,
                                                bottom = 7.sdp,
                                                start = 6.sdp,
                                                end = 13.sdp
                                            )
                                    )

                                }
                            }
                        }



                        if (screenType == Constants.WORKOUTS) {
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
                                modifier = Modifier.padding(start = 15.sdp, end = 10.sdp),
                                text = stringResource(R.string.you_must_try_these_workouts),
                                showBackButton = false,
                                navigateToViewAllData = navigateToWorkoutSeeMore
                            )
                        }



                        Box(
                            modifier = Modifier
                                .padding(top = 10.sdp, start = 15.sdp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            if (moreWorkoutsLoaderState) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.sdp)
                                        .padding(vertical = 20.sdp)
                                        .align(Center),
                                    strokeWidth = 3.sdp,
                                    color = MaterialTheme.colorScheme.onBackground,

                                    )
                            }

                            if (!moreWorkoutsLoaderState) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(15.sdp),
                                    modifier = Modifier.padding(bottom = 10.sdp)
                                ) {
                                    val data = workoutViewDetailModel.categoryWorkoutResponseData
                                    items(count = data.size, key = {
                                        data[it].id ?: 0
                                    }, itemContent = { index ->
                                        val workouts = data[index]

                                        val request = ImageRequest.Builder(context)
                                            .data(workouts.video?.thumbnail ?: "")
                                            .placeholder(R.drawable.workout_place_holder)
                                            .error(R.drawable.workout_place_holder)
                                            .fallback(R.drawable.workout_place_holder)
                                            .crossfade(true).build()

                                        WorkoutSubItem(
                                            modifier = Modifier
                                                .width(200.sdp)
                                                .height(120.sdp)
                                                .clickable {
                                                    navigateToWorkoutDetails(
                                                        data[index],
                                                        data[index].id.toString()
                                                    )
                                                },
                                            workouts = workouts,
                                            imageRequest = request,
                                            screenType = Constants.WORKOUTS,
                                            titleTextModifier = Modifier.padding(
                                                top = 7.sdp, bottom = 15.sdp
                                            )

                                        )
                                    })
                                }

                            }

                        }
                        Spacer(
                            modifier = Modifier.height(60.sdp)
                        )

                    }

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.sdp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier
                        .align(CenterStart)
                        .padding(start = 15.sdp)
                        .then(backPressClick)
                        .size(22.sdp),
                )

                if (!workoutViewDetailModel.isFeaturedWorkout) {
                    Image(
                        painter = if (workoutViewDetailModel.favouriteImageState) {
                            painterResource(id = R.drawable.ic_save_fav)
                        } else {
                            painterResource(id = R.drawable.ic_star)
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .align(CenterEnd)
                            .padding(end = 15.sdp)
                            .clickable {
                                workoutViewDetailModel.onEvent(
                                    WorkoutDetailUIEvent.AddFavouriteChanged(
                                        workoutViewDetailModel.workoutDetailObject.id
                                    )
                                )
                            }
                            .size(22.sdp),
                    )
                }

            }


            if (screenType == Constants.WORKOUTS) {
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .height(175.sdp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.bg_gradient),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.sdp)
                    .align(BottomStart)
            ) {
                ButtonComponentNoFullWidth(
                    value = stringResource(R.string.start_workout),
                    onButtonClicked = onNavigateToVideoPlayer,
                    modifier = Modifier
//                        .align(CenterStart)
                        .padding(end = 30.sdp),
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.background,
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = null,
                    modifier = Modifier
                        .align(CenterEnd)
                        .then(calendarClick)
                        .size(45.sdp),
                )

                DateCalendarBottomSheet(state = bottomSheetState,
                    screenType = Constants.WORKOUT_DETAIL_SCREEN,
                    selectDate = selectedDates,
                    startEndDate = { _, _ ->

                    })

            }

            if (workoutViewDetailModel.loaderState || playerBackPressState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Center),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,

                    )
            }
        }


    }
}