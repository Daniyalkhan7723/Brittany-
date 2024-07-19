package com.cp.brittany.dixon.ui.screens.home.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.favourites.FavouritesUiEvent
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.insight.InsightCard
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.allInsights
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.favourite.FavouritesInsightViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesInsidesScreen(
    favouriteInsightViewModel: FavouritesInsightViewModel = hiltViewModel(),
    navigateToInsideDetailScreen: (AllInsight) -> Unit,
    navigateToAllInsights: (String) -> Unit,
    sharedViewModel: SharedViewModel

) {
    favouriteInsightViewModel.apply {
        val state = rememberPullToRefreshState()
        val context = LocalContext.current
        val getFavouriteInsightResponse by favouriteInsideResponse.collectAsStateWithLifecycle()
        val addToFavouriteResponse by addFavouriteResponse.collectAsStateWithLifecycle()
        val sharedViewModelResponse by sharedViewModel.refreshFavouriteInsightListAfterLikeOrDislikeResponse.collectAsStateWithLifecycle()

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )

        val addToFavourite = remember<(Int) -> Unit> {
            { id ->
                onEvent(
                    FavouritesUiEvent.AddToFavourites(
                        id = id
                    )
                )
            }
        }

        val navigateToViewAllWorkouts = remember {
            {
                navigateToAllInsights(Constants.VIEW_ALL_INSIGHT_FAVOURITES)
            }
        }

        var swipeRefresh by remember {
            mutableStateOf(false)
        }


        LaunchedEffect(key1 = Unit) {
            when (sharedViewModelResponse) {
                true -> {
                    onEvent(
                        FavouritesUiEvent.GetFavouriteData
                    )
                    sharedViewModel.resetResponseForInsightFavourites()
                }

                false -> {

                }
            }
        }

        if (state.isRefreshing) {
            LaunchedEffect(true) {
                swipeRefresh = true
                onEvent(
                    FavouritesUiEvent.GetFavouriteData
                )
            }
        }


        when (getFavouriteInsightResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                state.endRefresh()
                if (getFavouriteInsightResponse.data?.status == true) {
                    sharedViewModel.refreshInsightListAfterLikeOrDislike(true)
                    showNoDataFoundOrNot =
                        getFavouriteInsightResponse.data?.data?.latest == null && getFavouriteInsightResponse.data?.data?.insights?.isEmpty() == true
                    getFavouriteInsightResponse.data?.data?.latest?.let {
                        latestInsight.value = it
                    }
                    getFavouriteInsightResponse.data?.data?.let { data ->
                        getFavouriteInsight.clear()
                        data.insights.let { list -> getFavouriteInsight.addAll(list) }
                    }


                } else {
                    showToast(
                        title = getFavouriteInsightResponse.data?.message ?: "",
                        isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                state.endRefresh()
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError =
                        JSONObject(getFavouriteInsightResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    getFavouriteInsightResponse.message ?: context.resources.getString(
                        R.string.something_went_wrong
                    )
                }

                showToast(
                    title = message, isSuccess = false
                )
            }

            is NetworkResult.Loading<*> -> {
                if (!swipeRefresh)
                    loaderState = true


                resetResponse()
            }

            is NetworkResult.NoInternet<*> -> {
                state.endRefresh()
                loaderState = false
                resetResponse()
                showToast(
                    title = getFavouriteInsightResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        when (addToFavouriteResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (addToFavouriteResponse.data?.status == true) {
                    onEvent(
                        FavouritesUiEvent.GetFavouriteData
                    )
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

        Box(
            modifier = Modifier
                .nestedScroll(state.nestedScrollConnection)
                .fillMaxSize()
        ) {
            if (!showNoDataFoundOrNot) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.sdp)
                ) {
                    item {
                        if (latestInsight.value.id != null) {
                            InsightCard(
                                latestInsight = latestInsight.value,
                                navigateToInsightsDetails = navigateToInsideDetailScreen,
                                modifier = Modifier
                                    .height(236.sdp)
                                    .fillMaxWidth(),
                                screenType = Constants.FAVOURITE,
                                addToFavourite = addToFavourite
                            )
                        }


                        if (latestInsight.value.id != null && getFavouriteInsight.isNotEmpty()) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.sdp)
                                    .height((2).sdp)
                                    .background(Color(0xFF35393e))
                            )
                        }

                        if (getFavouriteInsight.isNotEmpty()) {
                            ViewAllWorkoutsComponent(
                                modifier = Modifier,
                                text = stringResource(R.string.some_more_insights),
                                showBackButton = true,
                                navigateToViewAllData = navigateToViewAllWorkouts
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(15.sdp)
                            )
                        }


                    }
                    if (getFavouriteInsight.isNotEmpty()) {
                        allInsights(
                            allInsightsList = getFavouriteInsight,
                            navigateToInsightDetailsScreen = navigateToInsideDetailScreen,
                            modifier = Modifier
                        )
                    }


                }
            }

            if (loaderState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Alignment.Center),
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            if (showNoDataFoundOrNot) {
                NoDataFound(
                    modifier = Modifier.align(Alignment.Center)
                )
            }


//            PullToRefreshContainer(
//                contentColor = Color.White,
//                containerColor = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.align(Alignment.TopCenter),
//                state = state,
//            )


        }
    }
}