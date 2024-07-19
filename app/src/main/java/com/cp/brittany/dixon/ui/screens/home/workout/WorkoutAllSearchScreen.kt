package com.cp.brittany.dixon.ui.screens.home.workout

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.components.SearchedComponent
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.AllWorkoutsSubItem
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutTopBar
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutAllSearchViewModel
import com.cp.brittany.dixon.utills.Constants

import com.cp.brittany.dixon.utills.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutAllSearchScreen(
    workoutAllSearchViewModel: WorkoutAllSearchViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
    sharedViewModel: SharedViewModel,
    navigateToDetails: (String, String) -> Unit,
    screenType: String?

) {
    val context = LocalContext.current

    val workoutItems = workoutAllSearchViewModel.allWorkouts.collectAsLazyPagingItems()
    val search by workoutAllSearchViewModel.search.collectAsStateWithLifecycle()
    var httpError by remember { mutableStateOf(false) }
    var networkError by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val state = rememberPullToRefreshState()
    var swipeRefresh by remember {
        mutableStateOf(false)
    }

    val backPress = remember {
        {
            onBackPress()
        }
    }

    var showBackButton by remember {
        mutableStateOf(false)
    }

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            swipeRefresh = true
            workoutItems.refresh()
        }
    }

    val callBack = remember {
        {

        }
    }

    val searchClick = remember {
        {
            workoutAllSearchViewModel.isSearchVisible =
                !workoutAllSearchViewModel.isSearchVisible
        }
    }

    val onTextChanged = remember<(String) -> Unit> {
        {
            workoutAllSearchViewModel.setSearch(it)
        }
    }
    var showNoDataFoundOrNot by remember {
        mutableStateOf(false)
    }


    workoutAllSearchViewModel.apply {
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

                if (screenType == Constants.SCREEN_ALL_WORKOUTS_BY_CATEGORY || screenType == Constants.VIEW_ALL_WORKOUT_FAVOURITES) {
                    showBackButton = false
                    WorkoutTopBar(
                        title = workoutAllSearchViewModel.stateHandle.get<String>(Constants.CATEGORY_NAME)
                            ?: "",
                        backPress = backPress, searchClick = searchClick,
                        endIcon = R.drawable.ic_search,
                        backIconText = stringResource(id = R.string.workout)
                    )
                } else {
                    showBackButton = true
                }



                Spacer(modifier = Modifier.height(10.sdp))

                if (isSearchVisible || screenType == Constants.ALL_WORKOUT_SEARCH_SCREEN || screenType == Constants.SEARCH_ALL_WORKOUT_FAVOURITES) {
                    SearchedComponent(
                        labelValue = stringResource(R.string.search_here),
                        onQueryChanged = onTextChanged,
                        showBackButton = true,
                        layoutModifier = Modifier.padding(start = 10.sdp, end = 13.sdp),
                        query = search,
                        onBackPress = backPress
                    )
                }



                Spacer(modifier = Modifier.height(10.sdp))

                ViewAllWorkoutsComponent(
                    modifier = Modifier.padding(start = 12.sdp),
                    text = stringResource(id = R.string.all_workout),
                    showBackButton = false,
                    navigateToViewAllData = callBack
                )

                Spacer(modifier = Modifier.height(10.sdp))


                LazyColumn(state = listState) {
                    workoutItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                //You can add modifier to manage load state when first time response page is loading
                                if (!swipeRefresh)
                                    loaderState.value = true

                            }

                            loadState.refresh is LoadState.Error -> {
                                state.endRefresh()
                                loaderState.value = false
                                httpError = true
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
                            }
                        }
                    }

                    showNoDataFoundOrNot = workoutItems.itemCount == 0
                    items(count = workoutItems.itemCount, key = {
                        workoutItems[it]?.id ?: 0
                    }, itemContent = { index ->
                        workoutItems[index]?.let { workouts ->
                            val request =
                                ImageRequest.Builder(context).data(workouts.video?.thumbnail ?: "")
                                    .placeholder(R.drawable.place_holder_3)
                                    .error(R.drawable.place_holder_3)
                                    .fallback(R.drawable.place_holder_3).crossfade(true).build()

                            AllWorkoutsSubItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.sdp),
                                workouts = workouts,
                                imageRequest = request,
                                sharedViewModel = sharedViewModel,
                                navigateToDetails = navigateToDetails
                            )
                        }

                    })
                }

            }

            if (showNoDataFoundOrNot) {
                NoDataFound(
                    modifier = Modifier
                        .align(Alignment.Center)
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
        }
    }


}