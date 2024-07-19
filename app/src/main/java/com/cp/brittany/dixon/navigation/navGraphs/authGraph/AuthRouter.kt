package com.cp.brittany.dixon.navigation.navGraphs.authGraph

import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.Constants.Companion.SCREEN_TYPE

sealed class AuthRoute(val route: String) {
    object SplashScreen : AuthRoute(route = "splash_screen")
    object GetStartedScreen : AuthRoute(route = "get_started")
    object SignUpScreen : AuthRoute(route = "signup")
    object LoginScreen : AuthRoute(route = "login")
    object VerifyYourEmailScreen :
        AuthRoute(route = "verify_your_email/{${Constants.EMAIL}}/{${SCREEN_TYPE}}") {
        fun passData(email: String, type: String): String {
//            return this.route.replace(oldValue = "{${Constants.EMAIL}}", newValue = email)
            return "verify_your_email/$email/$type"
        }
    }

    object ForgotPasswordBaseScreen : AuthRoute("forgot_password_base_screen")
}