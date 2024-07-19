package com.cp.brittany.dixon.navigation.navGraphs.homeGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.DetailRoute
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.detailNavGraph
import com.cp.brittany.dixon.ui.screens.home.favourites.FavoritesScreen
import com.cp.brittany.dixon.ui.screens.home.insight.InsightScreen
import com.cp.brittany.dixon.ui.screens.home.calendar.CalendarScreen
import com.cp.brittany.dixon.ui.screens.home.profile.ProfileScreen
import com.cp.brittany.dixon.ui.screens.home.workout.WorkoutScreen
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.utills.Constants

@Composable
fun HomeNavGraph(
    navHostController: NavHostController,
    onLogout: () -> Unit,
    showHideBottomBar: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    isFromWorkoutPlayerScreen: (Boolean) -> Unit,
) {
    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = HomeScreenRoute.WorkoutScreen.route
    ) {
        composable(HomeScreenRoute.WorkoutScreen.route) {
            val navigateToDetails = remember<(String, String) -> Unit> {
                { categoryId, workoutId ->
                    navHostController.navigate(
                        route = DetailRoute.WorkoutDetail.passData(
                            categoryId = categoryId,
                            screenType = Constants.WORKOUTS,
                            workoutId = workoutId
                        )
                    )
                }
            }
            val navigateToAllWorkoutByCategory = remember<(String, String) -> Unit> {
                { id, name ->
                    navHostController.navigate(
                        route = DetailRoute.WorkoutAllSearch.passData(
                            id = id,
                            name = name,
                            screenType = Constants.SCREEN_ALL_WORKOUTS_BY_CATEGORY
                        )
                    )

                }
            }

            val navigateToWorkoutAllSearch = remember<(String) -> Unit> {
                { screenType ->
                    navHostController.navigate(
                        route = DetailRoute.WorkoutAllSearch.passData(screenType = screenType)
                    )
                }
            }

            val navigateToSubscriptionScreen = remember {
                {
                    navHostController.navigate(route = DetailRoute.SubscriptionPlan.route)
                }
            }

            val onShowHideBottomBar = remember<(Boolean) -> Unit> {
                {
                    showHideBottomBar(it)
                }
            }


            WorkoutScreen(
                navigateToDetails = navigateToDetails,
                navigateToAllWorkoutByCategory = navigateToAllWorkoutByCategory,
                showHideBottomBar = onShowHideBottomBar,
                sharedViewModel = sharedViewModel,
                navigateToAllWorkoutSearch = navigateToWorkoutAllSearch,
                navigateToSubscriptionScreen = navigateToSubscriptionScreen
            )
        }
        composable(HomeScreenRoute.InsightScreen.route) {
            val navigateToViewAllInsights = remember<(String) -> Unit> {
                { value ->
                    navHostController.navigate(
                        route = DetailRoute.AllInsights.passData(
                            value,
                        )
                    )
                }
            }

            val navigateToInsideDetailScreen = remember<(AllInsight) -> Unit> {
                {
                    sharedViewModel.sendInsightDataToDetailScreen(it)
                    navHostController.navigate(
                        route = DetailRoute.InsightDetails.route
                    )
                }
            }


            InsightScreen(
                navigateToAllInsights = navigateToViewAllInsights,
                navigateToInsideDetailScreen = navigateToInsideDetailScreen,
                sharedViewModel = sharedViewModel,
            )
        }
        composable(HomeScreenRoute.FavoritesScreen.route) {
            val navigateToWorkoutDetails = remember<(WorkoutData, String) -> Unit> {
                { workoutData, workoutId ->
//                    sharedViewModel.setWorkoutByDate(workoutData)
                    navHostController.navigate(
                        route = DetailRoute.WorkoutDetail.passData(
                            screenType = Constants.FAVOURITE, workoutId = workoutId
                        )
                    )
                }
            }

            val navigateToInsideDetailScreen = remember<(AllInsight) -> Unit> {
                {
                    sharedViewModel.sendInsightDataToDetailScreen(it)
                    navHostController.navigate(
                        route = DetailRoute.InsightDetails.route
                    )
                }
            }


            val navigateToViewAllInsights = remember<(String) -> Unit> {
                { screenType ->
                    navHostController.navigate(
                        route = DetailRoute.AllInsights.passData(
                            screenType,
                        )
                    )
                }
            }

            val navigateToWorkoutAllSearch = remember<(String) -> Unit> {
                { screenType ->
                    navHostController.navigate(
                        route = DetailRoute.WorkoutAllSearch.passData(screenType = screenType)
                    )
                }
            }
            val navigateToAllWorkoutByCategory = remember<(String, String) -> Unit> {
                { name, screenType ->
                    navHostController.navigate(
                        route = DetailRoute.WorkoutAllSearch.passData(
                            screenType = screenType,
                            name = name
                        )
                    )
                }
            }



            FavoritesScreen(
                navigateToFavouriteWorkoutDetails = navigateToWorkoutDetails,
                navigateToFavouriteInsightDetails = navigateToInsideDetailScreen,
                navigateToAllInsights = navigateToViewAllInsights,
                navigateToAllWorkouts = navigateToWorkoutAllSearch,
                navigateToAllWorkoutByCategory = navigateToAllWorkoutByCategory,
                sharedViewModel = sharedViewModel
            )
        }
        composable(HomeScreenRoute.CalendarScreen.route) {
            val navigateToAllScheduledWorkoutsScreen = remember {
                {
                    navHostController.navigate(
                        route = DetailRoute.AllScheduledWorkouts.route
                    )
                }
            }

            val navigateToScheduleWorkoutsDetailScreen = remember<(WorkoutData) -> Unit> {
                {
                    sharedViewModel.setWorkoutByDate(it)
                    navHostController.navigate(
                        route = DetailRoute.ScheduleWorkoutsDetails.route
                    )
                }
            }


            CalendarScreen(
                navigateToAllScheduledWorkoutsScreen = navigateToAllScheduledWorkoutsScreen,
                navigateToScheduleWorkoutsDetailScreen = navigateToScheduleWorkoutsDetailScreen,
                sharedViewModel = sharedViewModel
            )
        }

        composable(HomeScreenRoute.ProfileScreen.route) {
            val navigateToProfileSettingScreen = remember {
                {
                    moveToDetailScreen(navHostController, DetailRoute.ProfileDetail.route)

                }
            }
            ProfileScreen(navigateToProfileSettingScreen = navigateToProfileSettingScreen)
        }
        //detail graph
        detailNavGraph(
            navController = navHostController,
            onLogout = onLogout,
            sharedViewModel = sharedViewModel,
            isFromWorkoutPlayerScreen = isFromWorkoutPlayerScreen
        )
    }
}

private fun moveToDetailScreen(navHostController: NavHostController, routeName: String) {
    navHostController.navigate(routeName) {
    }
}