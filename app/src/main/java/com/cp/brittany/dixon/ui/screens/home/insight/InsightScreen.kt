package com.cp.brittany.dixon.ui.screens.home.insight

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.calendar.CalendarUIEvent
import com.cp.brittany.dixon.data.insights.InsightsUIEvent
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.HeaderWithSearch
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.InsightLists
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.allInsights
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.insight.InsightViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.getFormattedDateWithSmallMin
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightScreen(
    insightViewModel: InsightViewModel = hiltViewModel(),
    navigateToAllInsights: (String) -> Unit,
    navigateToInsideDetailScreen: (AllInsight) -> Unit,
    sharedViewModel: SharedViewModel
) {
    insightViewModel.apply {
        val state = rememberPullToRefreshState()

        val context = LocalContext.current
        val getInsightDateResponse by insightResponse.collectAsStateWithLifecycle()
        val sharedViewModelResponse by sharedViewModel.refreshInsightListAfterLikeOrDislikeResponse.collectAsStateWithLifecycle()

        val navigateToViewAllWorkouts = remember {
            {
                navigateToAllInsights(Constants.VIEW_ALL_INSIGHT)
            }
        }

        val navigateToSearchAllWorkouts = remember {
            {
                navigateToAllInsights(Constants.SEARCH_ALL_INSIGHT)
            }
        }
        LaunchedEffect(key1 = Unit) {
            offsetY1.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetY2.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetX3.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            offsetY3.animateTo(
                targetValue = 0f, animationSpec = tween(
                    durationMillis = Constants.ANIMATION_DURATION, easing = LinearEasing
                )
            )
        }

        var swipeRefresh by remember {
            mutableStateOf(false)
        }


        LaunchedEffect(key1 = Unit) {
            when (sharedViewModelResponse) {
                true -> {
                    insightViewModel.getInsightsData()
                    sharedViewModel.resetResponseForRefreshInsightListAfterLikeOrDislikeResponse()
                }

                false -> {

                }
            }
        }

        if (state.isRefreshing) {
            LaunchedEffect(true) {
                swipeRefresh = true
                onEvent(
                    InsightsUIEvent.Refresh
                )
            }
        }

        when (getInsightDateResponse) {
            is NetworkResult.Success<*> -> {
                workoutLoaderState = false
                state.endRefresh()
                if (getInsightDateResponse.data?.status == true) {
                    visibleInsightItems = true
                    getInsightDateResponse.data?.data?.let { data ->
                        getLatestInsights.clear()
                        data.latestInsights?.let { list -> getLatestInsights.addAll(list) }
                    }

                    getInsightDateResponse.data?.data?.let { data ->
                        getAllInsights.clear()
                        data.allInsight?.let { list -> getAllInsights.addAll(list) }
                    }
                } else {
                    showToast(
                        title = getInsightDateResponse.data?.message ?: "",
                        isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                state.endRefresh()
                resetResponse()
                workoutLoaderState = false
                val message: String = try {
                    val jObjError =
                        JSONObject(getInsightDateResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    getInsightDateResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                if (!swipeRefresh)
                    workoutLoaderState = true

                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                state.endRefresh()
                workoutLoaderState = false
                resetResponse()
                showToast(
                    title = getInsightDateResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        BrittanyDixonTheme {
            Box(
                modifier = Modifier
                    .nestedScroll(state.nestedScrollConnection)
                    .fillMaxSize()
            ) {

                if (visibleInsightItems) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 17.sdp, vertical = 40.sdp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(7.sdp))
                            HeaderWithSearch(
                                navigateToSearchAllInsights = navigateToSearchAllWorkouts,
                                title = stringResource(id = R.string.insight),
                                modifier = Modifier.offset {
                                    IntOffset(
                                        offsetX2.value.toInt(), offsetY2.value.toInt()
                                    )
                                }
                            )
                            InsightLists(
                                navigateToInsightsDetails = navigateToInsideDetailScreen,
                                getLatestInsights = getLatestInsights,
                                modifier = Modifier.offset {
                                    IntOffset(
                                        offsetX1.value.toInt(), offsetY1.value.toInt()
                                    )
                                }
                            )

                            if (getAllInsights.isNotEmpty()) {
                                ViewAllWorkoutsComponent(
                                    modifier = Modifier
                                        .padding(top = 15.sdp, bottom = 15.sdp)
                                        .offset {
                                            IntOffset(
                                                offsetX3.value.toInt(), offsetY3.value.toInt()
                                            )
                                        },
                                    text = stringResource(id = R.string.some_more_insights),
                                    showBackButton = true,
                                    navigateToViewAllData = navigateToViewAllWorkouts
                                )
                            }


                        }

                        allInsights(
                            allInsightsList = getAllInsights,
                            navigateToInsightDetailsScreen = navigateToInsideDetailScreen,
                            modifier = Modifier.offset {
                                IntOffset(
                                    offsetX3.value.toInt(), offsetY3.value.toInt()
                                )
                            }
                        )
                    }
                }

                if (workoutLoaderState) {
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


@Composable
fun InsightCard(
    latestInsight: AllInsight,
    modifier: Modifier = Modifier,
    navigateToInsightsDetails: (AllInsight) -> Unit,
    addToFavourite: (Int) -> Unit,
    screenType: String
) {

    Card(
        shape = RoundedCornerShape(15.dp), elevation = CardDefaults.cardElevation(5.dp), onClick = {
            navigateToInsightsDetails(latestInsight)
        }, modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            InsightImage(thumbnail = latestInsight.thumbnail ?: "")

            if (screenType == Constants.FAVOURITE) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star_selected),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.sdp, top = 10.sdp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            addToFavourite(latestInsight.id ?: 0)
                        }
                        .size(22.sdp),
                )
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 15.sdp, bottom = 10.sdp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    HeadingTextComponentWithoutFullWidth(
                        value = latestInsight.title ?: "",
                        textSize = 16.ssp,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )

                    Row(modifier = Modifier.padding(top = 5.sdp)) {
                        Row(modifier = Modifier) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds
                            )
                            Text(
                                text = latestInsight.duration?.getFormattedDateWithSmallMin()
                                    ?: "",
                                style = TextStyle(
                                    fontSize = 11.ssp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 5.sdp)

                            )
                        }

                        Row(modifier = Modifier.padding(start = 10.sdp)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_like),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds
                            )
                            Text(
                                text = "${latestInsight.likes.toString()} Likes",
                                style = TextStyle(
                                    fontSize = 11.ssp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 5.sdp)
                            )
                        }
                    }

                    if (screenType == Constants.INSIGHTS)
                        ButtonComponent(
                            value = stringResource(id = R.string.view_insight),
                            onButtonClicked = {
                                navigateToInsightsDetails(latestInsight)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.sdp, end = 10.sdp)
                                .height(35.sdp),
                            buttonColor = MaterialTheme.colorScheme.onPrimary,
                            textColor = MaterialTheme.colorScheme.onBackground
                        )

                }

            }
        }
    }

}

@Composable
fun InsightImage(modifier: Modifier = Modifier, thumbnail: String) {
    val context = LocalContext.current

    val imageRequest =
        ImageRequest.Builder(context).data(thumbnail).placeholder(R.drawable.place_holder_3)
            .error(R.drawable.place_holder_3).fallback(R.drawable.place_holder_3)
            .crossfade(enable = true).build()
    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
    Image(
        painter = painterResource(id = R.drawable.blur_view),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier.fillMaxSize()
    )
}
