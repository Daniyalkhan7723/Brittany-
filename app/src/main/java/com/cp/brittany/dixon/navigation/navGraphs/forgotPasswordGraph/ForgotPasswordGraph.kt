package com.cp.brittany.dixon.navigation.navGraphs.forgotPasswordGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.navigation.navGraphs.composable
import com.cp.brittany.dixon.ui.screens.auth.forgotPassword.ConfirmYourEmailScreen
import com.cp.brittany.dixon.ui.screens.auth.forgotPassword.ForgotPasswordScreen
import com.cp.brittany.dixon.ui.screens.auth.forgotPassword.SetUpNewPasswordScreen

@Composable
fun ForgotPasswordGraph(
    onButtonClicked: () -> Unit,
    onBackPressClicked: () -> Unit,
    onBaseBackPress: () -> Unit,
    dismissListener: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        route = Graph.FORGOT_PASSWORD,
        startDestination = ForgotPasswordRoute.ForgotPasswordAddEmailScreen.route
    ) {
        composable(
            route = ForgotPasswordRoute.ForgotPasswordAddEmailScreen.route
        ) {
            ForgotPasswordScreen(
                navController,
                onButtonClicked,
                onBaseBackPress,
                moveNextScreen = { email ->
                    navController.navigate(
                        route = ForgotPasswordRoute.ConfirmYourEmailScreen.passEmail(
                            email
                        )
                    )
                })
        }

        composable(
            route = ForgotPasswordRoute.ConfirmYourEmailScreen.route,
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) {
            ConfirmYourEmailScreen(
                navController,
                onButtonClicked,
                onBackPressClicked,
                moveNextScreen = {
                    navController.navigate(
                        route = ForgotPasswordRoute.SetUpNewPasswordScreen.passEmail(
                            it.arguments?.getString("email").toString()
                        )
                    )
                })
        }

        composable(route = ForgotPasswordRoute.SetUpNewPasswordScreen.route) {
            SetUpNewPasswordScreen(navController, onBackPressClicked, dismissListener = {
                dismissListener.invoke()
            })
        }

    }
}
