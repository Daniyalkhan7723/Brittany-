package com.cp.brittany.dixon.navigation.navGraphs.authGraph

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.navigation.navGraphs.composable
import com.cp.brittany.dixon.ui.screens.SplashScreen
import com.cp.brittany.dixon.ui.screens.auth.GetStartedScreen
import com.cp.brittany.dixon.ui.screens.auth.LoginScreen
import com.cp.brittany.dixon.ui.screens.auth.SignUpScreen
import com.cp.brittany.dixon.ui.screens.auth.VerifyYourEmailScreen
import com.cp.brittany.dixon.ui.screens.auth.forgotPassword.ForgotPasswordBaseScreen
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Constants.Companion.GO_HOME

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthRoute.SplashScreen.route,
    ) {


        composable(route = AuthRoute.SplashScreen.route) {
            SplashScreen(navController,
                navigateToNext = { screenType ->
                navController.navigate(screenType) {
                    popUpTo(AuthRoute.SplashScreen.route) {
                        inclusive = true
                    }
                }
            })

        }

        composable(route = AuthRoute.LoginScreen.route) {
            val moveVerifyEmailScreen = remember<(String, String) -> Unit> {
                { type, email ->
                    if (type == GO_HOME) {
                        navController.navigate(Graph.HOME) {
                            popUpTo(Graph.AUTHENTICATION) {
                                inclusive = true
                            }
                        }
                    } else {
                        if (email != "") {
                            navController.navigate(
                                route = AuthRoute.VerifyYourEmailScreen.passData(
                                    email,
                                    Constants.FROM_LOGIN
                                )
                            )
                        }

                    }
                }
            }
            LoginScreen(navController, moveNextScreen = moveVerifyEmailScreen)
        }

        composable(route = AuthRoute.SignUpScreen.route) {
            val moveVerifyEmailScreen = remember<(String) -> Unit> {
                {
                    navController.navigate(
                        route = AuthRoute.VerifyYourEmailScreen.passData(
                            it,
                            Constants.FROM_SIGN_UP
                        )
                    )
                }
            }
            SignUpScreen(navController, moveVerifyEmailScreen = moveVerifyEmailScreen)
        }

        composable(
            route = AuthRoute.VerifyYourEmailScreen.route,
            arguments = listOf(
                navArgument(Constants.EMAIL) {
                    type = NavType.StringType
                },
                navArgument(Constants.SCREEN_TYPE) {
                    type = NavType.StringType
                }
            )
        ) {
            VerifyYourEmailScreen(navController) {
                navController.navigate(Graph.HOME) {
                    popUpTo(Graph.AUTHENTICATION) {
                        inclusive = true
                    }
                }
            }
        }


        composable(route = AuthRoute.GetStartedScreen.route) {
            GetStartedScreen(navController)
        }

        composable(route = AuthRoute.ForgotPasswordBaseScreen.route) {
            ForgotPasswordBaseScreen(navController)
        }


    }

}