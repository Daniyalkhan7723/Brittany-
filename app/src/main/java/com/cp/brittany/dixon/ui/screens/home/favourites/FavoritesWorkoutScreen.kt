package com.cp.brittany.dixon.ui.screens.home.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.data.favourites.FavouritesUiEvent
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.components.NoDataFound
import com.cp.brittany.dixon.ui.components.ViewAllWorkoutsComponent
import com.cp.brittany.dixon.ui.components.showToast
import com.cp.brittany.dixon.ui.screens.home.favourites.favouritesComposables.favouriteWorkouts
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.WorkoutSubItem
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.favourite.FavouritesWorkoutViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.NetworkResult
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesWorkoutScreen(
    favouriteWorkoutViewModel: FavouritesWorkoutViewModel = hiltViewModel(),
    navigateToDetails: (WorkoutData, String) -> Unit,
    navigateToAllWorkoutByCategory: (String, String) -> Unit,
    sharedViewModel: SharedViewModel

) {
    favouriteWorkoutViewModel.apply {
        val context = LocalContext.current
        val sharedViewModelResponse by sharedViewModel.refreshFavouriteWorkoutListAfterLikeOrDislikeResponse.collectAsStateWithLifecycle()
        val state = rememberPullToRefreshState()

        val getFavouriteWorkoutResponse by favouriteWorkoutResponse.collectAsStateWithLifecycle()
        val addToFavouriteResponse by favouriteWorkoutViewModel.addToFavouriteResponse.collectAsStateWithLifecycle()


        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )

        val navigateToViewAllData = remember {
            {
                navigateToAllWorkoutByCategory(
                    Constants.ALL_FAVOURITE_WORKOUTS, Constants.VIEW_ALL_WORKOUT_FAVOURITES
                )
            }
        }

        val addToFavouritesClick by remember {
            mutableStateOf(Modifier.clickable {
                onEvent(
                    FavouritesUiEvent.AddToFavourites(
                        id = latestWorkout.value.id ?: 0
                    )
                )
            })
        }

        LaunchedEffect(key1 = Unit) {
            when (sharedViewModelResponse) {
                true -> {
                    onEvent(
                        FavouritesUiEvent.GetFavouriteData
                    )
                    sharedViewModel.resetResponseForWorkoutFavourites()
                }

                false -> {

                }
            }
        }
        when (getFavouriteWorkoutResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (getFavouriteWorkoutResponse.data?.status == true) {
                    showNoDataFoundOrNot =
                        getFavouriteWorkoutResponse.data?.data?.latest_workout == null && getFavouriteWorkoutResponse.data?.data?.favourited_workout?.isEmpty() == true

                    getFavouriteWorkoutResponse.data?.data?.latest_workout?.let {
                        latestWorkout.value = it
                    }
                    getFavouriteWorkoutResponse.data?.data?.let { data ->
                        getFavouriteWorkouts.clear()
                        data.favourited_workout.let { list -> getFavouriteWorkouts.addAll(list) }
                    }
                } else {
                    showToast(
                        title = getFavouriteWorkoutResponse.data?.message ?: "", isSuccess = false
                    )
                }
                resetResponse()
            }

            is NetworkResult.Error<*> -> {
                resetResponse()
                loaderState = false
                val message: String = try {
                    val jObjError = JSONObject(getFavouriteWorkoutResponse.message.toString())
                    jObjError.get("message").toString()
                } catch (e: Exception) {
                    getFavouriteWorkoutResponse.message ?: context.resources.getString(
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
                    title = getFavouriteWorkoutResponse.message ?: "", isSuccess = false
                )

            }

            is NetworkResult.NoCallYet<*> -> {

            }
        }

        when (addToFavouriteResponse) {
            is NetworkResult.Success<*> -> {
                loaderState = false
                if (addToFavouriteResponse.data?.status == true) {
                    sharedViewModel.refreshWorkoutListAfterLikeOrDislike(true)
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
                .fillMaxSize()
                .nestedScroll(state.nestedScrollConnection)
        ) {

            if (!showNoDataFoundOrNot) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.sdp)
                ) {
                    item {
                        val request =
                            ImageRequest.Builder(context).data(latestWorkout.value.video?.thumbnail)
                                .placeholder(R.drawable.workout_place_holder)
                                .error(R.drawable.workout_place_holder)
                                .fallback(R.drawable.workout_place_holder).crossfade(true).build()

                        if (latestWorkout.value.id != null) {
                            WorkoutSubItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(178.sdp)
                                    .clickable {
                                        navigateToDetails(
                                            latestWorkout.value,
                                            latestWorkout.value.id.toString()
                                        )
                                    },
                                workouts = latestWorkout.value,
                                imageRequest = request,
                                screenType = Constants.FAVOURITE,
                                titleTextModifier = Modifier.padding(
                                    vertical = 10.sdp
                                ),
                                likeDislikeModifier = Modifier.then(addToFavouritesClick),
                            )
                        }

                        if (latestWorkout.value.id != null && getFavouriteWorkouts.isNotEmpty()) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.sdp)
                                    .height((2).sdp)
                                    .background(Color(0xFF35393e))
                            )
                        }


                        if (getFavouriteWorkouts.isNotEmpty()) {
                            ViewAllWorkoutsComponent(
                                modifier = Modifier.padding(
                                    top = 5.sdp
                                ),
                                text = stringResource(R.string.explore_previous_workouts),
                                showBackButton = true,
                                navigateToViewAllData = navigateToViewAllData
                            )
                        }


                    }

                    if (getFavouriteWorkouts.isNotEmpty()) {
                        favouriteWorkouts(
                            workoutData = getFavouriteWorkouts,
                            navigateToWorkoutDetailsScreen = navigateToDetails
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(50.sdp))
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

        }

    }


}