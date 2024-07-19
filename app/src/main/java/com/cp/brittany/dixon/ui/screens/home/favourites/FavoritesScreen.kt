package com.cp.brittany.dixon.ui.screens.home.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.ui.screens.home.favourites.favouritesComposables.SegmentedControl
import com.cp.brittany.dixon.ui.screens.home.insight.insightComposables.HeaderWithSearch
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.favourite.FavouritesViewModel
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.sdp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun FavoritesScreen(
    favouritesViewModel: FavouritesViewModel = hiltViewModel(),
    navigateToFavouriteWorkoutDetails: (WorkoutData,String) -> Unit,
    navigateToFavouriteInsightDetails: (AllInsight) -> Unit,
    navigateToAllInsights: (String) -> Unit,
    navigateToAllWorkouts: (String) -> Unit,
    navigateToAllWorkoutByCategory: (String, String) -> Unit,
    sharedViewModel: SharedViewModel

) {
    favouritesViewModel.apply {
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )

        val itemSelection = remember<(Int) -> Unit> {
            { index ->
                segmentState.value = index
            }
        }

        val navigateToSearchScreen = remember {
            {
                if (segmentState.value == 0) {
                    navigateToAllWorkouts(Constants.SEARCH_ALL_WORKOUT_FAVOURITES)
                } else {
                    navigateToAllInsights(Constants.SEARCH_ALL_INSIGHT_FAVOURITES)
                }
            }
        }


        BrittanyDixonTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(bottom = 65.sdp)) {
                    HeaderWithSearch(
                        navigateToSearchAllInsights = navigateToSearchScreen,
                        modifier = Modifier.padding(top = 45.sdp, start = 15.sdp, end = 15.sdp),
                        title = stringResource(R.string.favorites)
                    )

                    Spacer(modifier = Modifier.padding(top = 15.sdp))

                    SegmentedControl(
                        modifier = Modifier,
                        onItemSelection = itemSelection,
                        defaultSelectedItemIndex = segmentState.value
                    )

                    Spacer(modifier = Modifier.padding(top = 10.sdp))

                    if (segmentState.value == 0)
                        FavoritesWorkoutScreen(
                            navigateToDetails = navigateToFavouriteWorkoutDetails,
                            navigateToAllWorkoutByCategory = navigateToAllWorkoutByCategory,
                            sharedViewModel = sharedViewModel
                        )
                    else
                        FavoritesInsidesScreen(
                            navigateToInsideDetailScreen = navigateToFavouriteInsightDetails,
                            navigateToAllInsights = navigateToAllInsights,
                            sharedViewModel = sharedViewModel

                        )
                }


            }
        }
    }


}