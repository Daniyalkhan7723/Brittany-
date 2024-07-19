package com.cp.brittany.dixon.ui.screens.home.workout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.workout.WorkoutUIEvent
import com.cp.brittany.dixon.model.workout.Data
import com.cp.brittany.dixon.ui.components.BoldTextComponent
import com.cp.brittany.dixon.ui.components.NormalTextComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.BuyPremiumComposable
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.FeatureWorkoutComposable
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutItem
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Constants.Companion.ALPHA_DURATION
import com.cp.brittany.dixon.utills.Constants.Companion.ANIMATION_DURATION
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    navigateToDetails: (String, String) -> Unit,
    navigateToAllWorkoutByCategory: (String, String) -> Unit,
    navigateToAllWorkoutSearch: (String) -> Unit,
    showHideBottomBar: (Boolean) -> Unit,
    navigateToSubscriptionScreen: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var visiblePremiumBanner by remember { mutableStateOf(false) }
    val featureWorkoutResponse by workoutViewModel.featureWorkoutResponse.collectAsStateWithLifecycle()
    val categoryWorkoutResponse by workoutViewModel.categoryWorkoutResponse.collectAsStateWithLifecycle()
    val sharedViewModelResponse by sharedViewModel.refreshWorkoutListAfterLikeOrDislikeResponse.collectAsStateWithLifecycle()
    val state = rememberPullToRefreshState()
    var swipeRefresh by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.alpha.animateTo(
            1.0f, animationSpec = tween(ALPHA_DURATION)
        )
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.offsetX3.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.offsetY1.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.offsetY2.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.offsetY3.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    if (workoutViewModel.preference.IsFromSignUp()) {
        LaunchedEffect(key1 = Unit) {
            delay(1500)  // the delay of 3 seconds
            workoutViewModel.visibleTutorial = true

        }
    }


    LaunchedEffect(key1 = Unit) {
        when (sharedViewModelResponse) {
            true -> {
                workoutViewModel.onEvent(
                    WorkoutUIEvent.RefreshData
                )
                sharedViewModel.resetResponseForRefreshWorkoutListAfterLikeOrDislikeResponse()
            }

            false -> {

            }
        }
    }

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            swipeRefresh = true
            workoutViewModel.onEvent(
                WorkoutUIEvent.RefreshData
            )

        }
    }


    val onNavigateToAllWorkoutByCategory = remember<(Data) -> Unit> {
        { data ->
            navigateToAllWorkoutByCategory(
                data.id.toString(),
                data.name ?: "",
            )
        }

    }

    workoutViewModel.apply {
        when (categoryWorkoutResponse) {
            is NetworkResult.Success<*> -> {
                if (categoryWorkoutResponse.data?.status == true) {
                    categoryWorkoutResponseData.clear()
                    categoryWorkoutResponse.data?.data?.let {
                        categoryWorkoutResponseData.addAll(it)
                    }
                } else {
                    showToast(
                        title = featureWorkoutResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                state.endRefresh()
                workoutLoaderState.value = false
                val message: String = try {
                    val jObjError = JSONObject(categoryWorkoutResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    categoryWorkoutResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                if (!swipeRefresh) workoutLoaderState.value = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                state.endRefresh()
                workoutLoaderState.value = false
                visibleCategoryWorkout.value = false
                resetResponse()
                showToast(
                    title = categoryWorkoutResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        when (featureWorkoutResponse) {
            is NetworkResult.Success<*> -> {
                workoutLoaderState.value = false
                state.endRefresh()
                if (featureWorkoutResponse.data?.status == true) {
                    featureWorkoutResponse.data?.data?.let {
                        visibleFeaturedWorkout.value = true
                        visibleCategoryWorkout.value = true
                        getFeatureWorkoutResponseData.value = it
                    }
                } else {
                    visibleCategoryWorkout.value = false
                    visibleFeaturedWorkout.value = false
                    showToast(
                        title = featureWorkoutResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                state.endRefresh()
                visibleCategoryWorkout.value = false
                visibleFeaturedWorkout.value = false
                workoutLoaderState.value = false

                resetResponse()
                val message: String = try {
                    val jObjError = JSONObject(featureWorkoutResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    featureWorkoutResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                if (!swipeRefresh) workoutLoaderState.value = true
                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                state.endRefresh()
                visibleCategoryWorkout.value = false
                visibleFeaturedWorkout.value = false
                workoutLoaderState.value = false
                resetResponse()
                showToast(
                    title = featureWorkoutResponse.message ?: "", isSuccess = false
                )
            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        BrittanyDixonTheme {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Color.Transparent
            )

            Box {
                Box(
                    modifier = if (visibleTutorial) Modifier
                        .fillMaxSize()
                        .alpha(alpha.value)
                        .nestedScroll(state.nestedScrollConnection)
                    else Modifier
                        .fillMaxSize()
                        .alpha(alpha.value)
                        .padding(top = 20.sdp)
                        .nestedScroll(state.nestedScrollConnection)


                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally
                    ) {
                        if (visibleFeaturedWorkout.value) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .offset {
                                    IntOffset(
                                        offsetX2.value.toInt(), offsetY2.value.toInt()
                                    )
                                }
                                .padding(top = 25.sdp)) {
                                Image(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .width(140.sdp)
                                        .height(32.sdp)
                                        .align(CenterEnd),
                                    painter = painterResource(id = R.drawable.logo_glampian_splash),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = "Search",
                                    modifier = Modifier
                                        .align(CenterEnd)
                                        .padding(end = 10.sdp)
                                        .clickable {
                                            navigateToAllWorkoutSearch(Constants.ALL_WORKOUT_SEARCH_SCREEN)
                                        }
                                        .size(22.sdp),
                                )
                            }

                        }

                        if (visibleCategoryWorkout.value) {
                            LazyColumn(contentPadding = PaddingValues(
                                bottom = 120.sdp,
                                end = 10.sdp
                            ),
                                modifier = Modifier.offset {
                                    IntOffset(
                                        offsetX3.value.toInt(), offsetY3.value.toInt()
                                    )
                                }) {
                                val data = categoryWorkoutResponseData
                                items(count = data.size, key = {
                                    data[it].id ?: 0
                                }, itemContent = { index ->
                                    val workoutItemData = data[index]
                                    visiblePremiumBanner =
                                        (data.size > 1 && index == data.size - 2) || (data.size == 1)

                                    if (index == 0) {
                                        if (visibleFeaturedWorkout.value) {
                                            FeatureWorkoutComposable(
                                                navigateToDetails = navigateToDetails,
                                                modifier = Modifier.offset {
                                                    IntOffset(
                                                        offsetX1.value.toInt(),
                                                        offsetY1.value.toInt()
                                                    )
                                                },
                                                sharedViewModel = sharedViewModel
                                            )
                                        }

                                    }

                                    WorkoutItem(
                                        modifier = Modifier,
                                        navigateToDetails = navigateToDetails,
                                        workoutItemData = workoutItemData,
                                        sharedViewModel = sharedViewModel,
                                        navigateToWorkoutSeeMore = onNavigateToAllWorkoutByCategory,
                                        indexValue = index

                                    )

                                    Column {
//                                        if (visiblePremiumBanner && workoutViewModel.preference.getUser()?.is_premium == false) {
                                        if (visiblePremiumBanner) {
                                            BuyPremiumComposable(modifier = Modifier
                                                .clickable {
                                                    navigateToSubscriptionScreen()
                                                }
                                                .padding(
                                                    top = 20.sdp,
                                                    bottom = 20.sdp,
                                                    start = 15.sdp,
                                                    end = 8.sdp
                                                )
                                            )
                                        }
                                    }

                                })
                            }
                        }
                    }

                    if (preference.IsFromSignUp()) {
                        if (visibleTutorial) {
                            showHideBottomBar(false)
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    coroutineScope.launch {
                                        preference.saveIsFromSignUp(false)
                                        showHideBottomBar(true)
                                        visibleTutorial = false
                                    }
                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.bg_layer),
                                    contentDescription = "getStartedImage",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.matchParentSize()
                                )
                                Column {
                                    Spacer(
                                        modifier = Modifier
                                            .padding(top = 25.sdp)
                                            .width(150.sdp)
                                            .height(35.sdp)
                                    )
                                    FeatureWorkoutComposable(
                                        navigateToDetails = navigateToDetails,
                                        sharedViewModel = sharedViewModel
                                    )

                                    Spacer(modifier = Modifier.height(10.sdp))

                                    Image(
                                        painter = painterResource(id = R.drawable.bg_weight_lifting),
                                        contentDescription = null,
                                        modifier = Modifier.padding(start = 80.sdp)
                                    )
                                    Spacer(modifier = Modifier.height(4.sdp))

                                    BoldTextComponent(
                                        value = "Cardio Blast Total Body",
                                        textSize = 15.ssp,
                                        textColor = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.align(CenterHorizontally)
                                    )

                                    Spacer(modifier = Modifier.height(2.sdp))

                                    NormalTextComponent(
                                        value = "Need to boost your kaboost? Give this routine a try! Integer et elit eget elit facilisis tristique. Nam vel iaculis mauris.",
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                            .padding(horizontal = 35.sdp)
                                            .heightIn(min = 40.sdp),
                                        textSize = 12.ssp,
                                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    )
                                }

                            }
                        }
                    }

                }

                if (workoutLoaderState.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.sdp)
                            .align(Alignment.Center),
                        strokeWidth = 3.sdp,
                        color = MaterialTheme.colorScheme.onBackground,

                        )
                }


                PullToRefreshContainer(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = state,
                )

            }


        }

    }
}



