package com.cp.brittany.dixon.ui.screens.home.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.components.NormalTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.SearchedComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.AllInsightsItem
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutTopBar
import com.cp.brittany.dixon.ui.viewModel.insight.AllInsightViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllInsightsScreen(
    allInsightsViewModel: AllInsightViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
    navigateToInsideDetailScreen: (AllInsight) -> Unit,
    screenType: String?

) {
    val context = LocalContext.current

    val insightItems = allInsightsViewModel.insights.collectAsLazyPagingItems()
    val search by allInsightsViewModel.search.collectAsStateWithLifecycle()

    var httpError by remember { mutableStateOf(false) }
    var networkError by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    val listState = rememberLazyListState()

    var swipeRefresh by remember {
        mutableStateOf(false)
    }

    val backPress = remember {
        {
            onBackPress()
        }
    }

    val searchClick = remember {
        {
            allInsightsViewModel.isSearchVisible = !allInsightsViewModel.isSearchVisible
        }
    }

    val onTextChanged = remember<(String) -> Unit> {
        {
            allInsightsViewModel.setSearch(it)
        }
    }

    var showNoDataFoundOrNot by remember {
        mutableStateOf(false)
    }

    var showBackButton by remember {
        mutableStateOf(false)
    }

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            swipeRefresh = true
            insightItems.refresh()
        }
    }


    allInsightsViewModel.apply {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.sdp)
                .nestedScroll(state.nestedScrollConnection)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart,

            ) {
            Column(
                modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.sdp))

                if (screenType == Constants.VIEW_ALL_INSIGHT || screenType == Constants.VIEW_ALL_INSIGHT_FAVOURITES) {
                    showBackButton = false
                    WorkoutTopBar(
                        title = stringResource(id = R.string.all_insights),
                        backPress = backPress, searchClick = searchClick,
                        endIcon = R.drawable.ic_search,
                        backIconText = stringResource(id = R.string.insight),
                    )

                    Spacer(modifier = Modifier.height(10.sdp))

                } else {
                    showBackButton = true
                }


                if (isSearchVisible || screenType == Constants.SEARCH_ALL_INSIGHT || screenType == Constants.SEARCH_ALL_INSIGHT_FAVOURITES) {
                    SearchedComponent(
                        labelValue = stringResource(R.string.search_here),
                        onQueryChanged = onTextChanged,
                        showBackButton = showBackButton,
                        layoutModifier = Modifier.padding(horizontal = 10.sdp),
                        query = search,
                        onBackPress = backPress
                    )
                }



                if (screenType == Constants.SEARCH_ALL_INSIGHT) {
                    Spacer(modifier = Modifier.height(15.sdp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        NormalTextComponentWithoutFullWidth(
                            value = stringResource(R.string.suggested_insights),
                            textSize = 14.ssp,
                            textColor = MaterialTheme.colorScheme.onSecondary,
                            fontStyle = FontStyle.Normal,
                            modifier = Modifier
                                .padding(start = 12.sdp)

                        )
                    }
                }


                Spacer(modifier = Modifier.height(10.sdp))


                LazyColumn(state = listState) {

                    insightItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                //You can add modifier to manage load state when first time response page is loading
                                //You can add modifier to manage load state when first time response page is loading
                                if (!swipeRefresh) loaderState.value = true
                            }

                            loadState.refresh is LoadState.Error -> {
                                loaderState.value = false
                                httpError = true
                                state.endRefresh()
                                //You can add modifier to manage load state when first time response page is loading
                            }

                            loadState.refresh is LoadState.NotLoading -> {
                                state.endRefresh()
                                //You can add modifier to manage load state when first time response page is loading
                                loaderState.value = false
                            }

                            loadState.append is LoadState.Loading -> {

                                //You can add modifier to manage load state when next response page is loading
                            }

                            loadState.append is LoadState.Error -> {
                                state.endRefresh()
                                networkError = true
                                loaderState.value = false

                                //You can use modifier to show error message
                            }
                        }
                    }
                    showNoDataFoundOrNot = insightItems.itemCount == 0


                    items(insightItems.itemCount) { index ->
                        insightItems[index]?.let { insights ->
                            AllInsightsItem(modifier = Modifier
                                .fillMaxWidth()
                                .height(170.sdp),
                                allInsight = insights,
                                navigateToInsightDetailsScreen = {
                                    navigateToInsideDetailScreen(insights)
                                })
                        }
                    }

                }

            }

            if (showNoDataFoundOrNot) {
                NoDataFound(
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            if (httpError) {
                showToast(
                    title = "Something went wrong", isSuccess = false
                )
            } else if (networkError) {
                showToast(
                    title = "No Internet", isSuccess = false
                )
            }


            if (loaderState.value) {
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