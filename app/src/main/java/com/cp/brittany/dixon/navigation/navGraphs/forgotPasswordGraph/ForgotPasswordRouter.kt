package com.cp.brittany.dixon.navigation.navGraphs.forgotPasswordGraph

import com.cp.brittany.dixon.utills.Constants.Companion.EMAIL

sealed class ForgotPasswordRoute(val route: String) {
    object ForgotPasswordAddEmailScreen : ForgotPasswordRoute(route = "forgot_password_add_email")
    object ConfirmYourEmailScreen :
        ForgotPasswordRoute(route = "confirm_your_email_screen/{$EMAIL}") {
        fun passEmail(email: String): String {
            return this.route.replace(oldValue = "{$EMAIL}", newValue = email)
        }
    }

    object SetUpNewPasswordScreen :
        ForgotPasswordRoute(route = "set_up_new_password_screen/{$EMAIL}") {
        fun passEmail(email: String): String {
            return this.route.replace(oldValue = "{$EMAIL}", newValue = email)
        }
    }
}