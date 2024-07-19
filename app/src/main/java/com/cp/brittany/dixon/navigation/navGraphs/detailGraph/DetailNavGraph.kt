package com.cp.brittany.dixon.navigation.navGraphs.detailGraph

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.android.billingclient.api.ProductDetails
import com.cp.brittany.dixon.model.insight.AllInsight
import com.cp.brittany.dixon.model.workout.WorkoutData
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.ui.screens.home.calendar.AllScheduleWorkoutsScreen
import com.cp.brittany.dixon.ui.screens.home.calendar.ScheduledWorkoutDetailScreen
import com.cp.brittany.dixon.ui.screens.home.calendar.PreviewImageScreen
import com.cp.brittany.dixon.ui.screens.home.insight.AllInsightsScreen
import com.cp.brittany.dixon.ui.screens.home.insight.InsightDetailScreen
import com.cp.brittany.dixon.ui.screens.home.profile.ProfileSettingScreen
import com.cp.brittany.dixon.ui.screens.home.profile.ProfileEditScreen
import com.cp.brittany.dixon.ui.screens.home.profile.ResetPasswordScreen
import com.cp.brittany.dixon.ui.screens.home.subscriptions.SubscriptionUpgrade
import com.cp.brittany.dixon.ui.screens.home.subscriptions.SubscriptionProductDetailScreen
import com.cp.brittany.dixon.ui.screens.home.subscriptions.SubscriptionProductsScreen
import com.cp.brittany.dixon.ui.screens.home.workout.WorkoutAllSearchScreen
import com.cp.brittany.dixon.ui.screens.home.workout.WorkoutDetailScreen
import com.cp.brittany.dixon.ui.screens.home.workout.WorkoutPlayerScreen
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.utills.ChooseSubscription
import com.cp.brittany.dixon.utills.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController, onLogout: () -> Unit, sharedViewModel: SharedViewModel,
    isFromWorkoutPlayerScreen: (Boolean) -> Unit,
) {
    navigation(
        route = Graph.DETAILS, startDestination = DetailRoute.WorkoutDetail.route
    ) {

        composable(
            route = DetailRoute.WorkoutDetail.route,
            arguments = listOf(
                navArgument(Constants.CATEGORY_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(Constants.WORKOUT_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(Constants.SCREEN_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) { navBackStackEntry ->
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val navigateToWorkoutDetails = remember<(WorkoutData, String) -> Unit> {
                { workoutData, workoutId ->
                    navController.navigate(
                        route = DetailRoute.WorkoutDetail.passData(
                            categoryId = workoutData.category_id.toString(),
                            screenType = Constants.WORKOUTS,
                            workoutId = workoutId
                        )
                    )

                }
            }

            val navigateToVideoPlayer = remember<(String, String, String, String, Int) -> Unit> {
                { url, screenType, workoutId, watchTime, progress ->
                    if (screenType == Constants.WORKOUT_PLAYER_SCREEN) {
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            DetailRoute.WorkoutPlayer.passData(
                                encodedUrl,
                                workoutId,
                                watchTime,
                                progress
                            )
                        )
                    } else {
                        navController.navigate(
                            DetailRoute.SubscriptionPlan.route
                        )
                    }

                }
            }

            val screenType = navBackStackEntry.arguments?.getString(Constants.SCREEN_TYPE)
            WorkoutDetailScreen(
                sharedViewModel = sharedViewModel,
                onBackPress = onBackPress,
                navigateToVideoPlayer = navigateToVideoPlayer,
                screenType = screenType ?: "",
                navigateToWorkoutDetails = navigateToWorkoutDetails
            )
        }

        composable(route = DetailRoute.SubscriptionPlan.route) { navBackStackEntry ->
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val navigateToPackageDetailScreen =
                remember<(ProductDetails, Int) -> Unit> {
                    { productDetail, packageId ->
                        sharedViewModel.sendPackageDataToPackageDetail(productDetail)
//                        sharedViewModel.sendChooseSubscriptionObject(chooseSubscription)

                        navController.navigate(
                            route = DetailRoute.SubscriptionDetail.passData(
                                packageId
                            )
                        )
                    }
                }

            SubscriptionProductsScreen(
                onBackPress = onBackPress,
            )
        }


        composable(route = DetailRoute.SubscriptionDetail.route,
            arguments = listOf(navArgument(Constants.PACKAGE_ID) {
                type = NavType.IntType
            }
            )
        ) { navBackStackEntry ->
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val navigateToHomeScreen = remember {
                {
                    navController.navigate(Graph.HOME) {
                        popUpTo(Graph.HOME) {
                            inclusive = true
                        }

                    }

                }
            }
            val packageId = navBackStackEntry.arguments?.getInt(Constants.PACKAGE_ID)
            SubscriptionProductDetailScreen(
                onBackPress = onBackPress,
                sharedViewModel = sharedViewModel,
                packageId = packageId ?: 0,
                navigateToHomeScreen = navigateToHomeScreen

            )
        }


        composable(route = DetailRoute.ImagePreview.route,
            arguments = listOf(navArgument(Constants.IMAGE_URL) {
                type = NavType.StringType
            }, navArgument(Constants.IMAGE_ID) {
                type = NavType.StringType
            }

            )) { navBackStackEntry ->
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            val image = navBackStackEntry.arguments?.getString(Constants.IMAGE_URL)
            val imageId = navBackStackEntry.arguments?.getString(Constants.IMAGE_ID)
            PreviewImageScreen(
                onBackPress = onBackPress,
                imageUrl = image ?: "",
                imageId = imageId ?: "",
                sharedViewModel = sharedViewModel,
            )
        }

        composable(
            route = DetailRoute.ScheduleWorkoutsDetails.route,
        ) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val onNavigateToImagePreviousScreen = remember<(String, String) -> Unit> {
                { url, imageId ->
                    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        route = DetailRoute.ImagePreview.passData(
                            encodedUrl, imageId
                        )
                    )
                }
            }
            ScheduledWorkoutDetailScreen(
                sharedViewModel = sharedViewModel,
                onBackPress = onBackPress,
                onNavigateToImagePreviousScreen = onNavigateToImagePreviousScreen
            )
        }

        composable(
            route = DetailRoute.InsightDetails.route,
        ) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }


            InsightDetailScreen(
                sharedViewModel = sharedViewModel,
                onBackPress = onBackPress,
            )
        }

        composable(
            route = DetailRoute.WorkoutPlayer.route,
            arguments = listOf(navArgument(Constants.VIDEO_URL) {
                type = NavType.StringType
            },
                navArgument(Constants.WORKOUT_ID) {
                    type = NavType.StringType
                },
                navArgument(Constants.WATCH_TIME) {
                    type = NavType.StringType
                },
                navArgument(Constants.PROGRESS) {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            WorkoutPlayerScreen(
                isFromWorkoutPlayerScreen = isFromWorkoutPlayerScreen,
                onBackPress = onBackPress,
                sharedViewModel = sharedViewModel,
                workoutId = navBackStackEntry.arguments?.getString(Constants.WORKOUT_ID) ?: "",
            )
        }


        composable(
            route = DetailRoute.AllInsights.route,
            arguments = listOf(navArgument(Constants.SCREEN_TYPE) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val screenType = navBackStackEntry.arguments?.getString(Constants.SCREEN_TYPE)
            val navigateToInsideDetailScreen = remember<(AllInsight) -> Unit> {
                {
                    sharedViewModel.sendInsightDataToDetailScreen(it)
                    navController.navigate(
                        route = DetailRoute.InsightDetails.route
                    )
                }
            }

            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            AllInsightsScreen(
                onBackPress = onBackPress,
                navigateToInsideDetailScreen = navigateToInsideDetailScreen,
                screenType = screenType
            )
        }


        composable(
            route = DetailRoute.WorkoutAllSearch.route,
            arguments = listOf(navArgument(Constants.SCREEN_TYPE) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val screenType = navBackStackEntry.arguments?.getString(Constants.SCREEN_TYPE)
            val navigateToDetails = remember<(String, String) -> Unit> {
                { categoryId, workoutId ->
                    navController.navigate(
                        route = DetailRoute.WorkoutDetail.passData(
                            categoryId = categoryId,
                            screenType = Constants.WORKOUTS,
                            workoutId = workoutId
                        )
                    )
                }
            }
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            WorkoutAllSearchScreen(
                onBackPress = onBackPress,
                sharedViewModel = sharedViewModel,
                navigateToDetails = navigateToDetails,
                screenType = screenType
            )
        }

        composable(
            route = DetailRoute.AllScheduledWorkouts.route,
        ) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val navigateToScheduleWorkoutsDetailScreen = remember<(WorkoutData) -> Unit> {
                { workoutData ->
                    sharedViewModel.setWorkoutByDate(workoutData)
                    navController.navigate(
                        route = DetailRoute.ScheduleWorkoutsDetails.route
                    )
                }
            }

            AllScheduleWorkoutsScreen(
                onBackPress = onBackPress,
                navigateToScheduleWorkoutsDetailScreen = navigateToScheduleWorkoutsDetailScreen
            )
        }

        composable(DetailRoute.ProfileDetail.route) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            val onClickLogout = remember {
                {
                    onLogout()
                }
            }
            ProfileSettingScreen(
                navController = navController, onLogout = onClickLogout, onBackPress = onBackPress
            )
        }

        composable(DetailRoute.ProfileEdit.route) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            ProfileEditScreen(
                onBackPress = onBackPress,
            )
        }

        composable(DetailRoute.ResetPassword.route) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }
            ResetPasswordScreen(
                onBackPress = onBackPress,
            )
        }

        composable(DetailRoute.SubscriptionInfo.route) {
            val onBackPress = remember<() -> Unit> {
                {
                    navController.popBackStack()
                }
            }

            val navigateToSubscriptionScreen = remember {
                {
                    navController.navigate(route = DetailRoute.SubscriptionPlan.route)
                }
            }

            SubscriptionUpgrade(
                onBackPress = onBackPress,
                navigateToSubscriptionProductsScreen = navigateToSubscriptionScreen
            )
        }

    }
}