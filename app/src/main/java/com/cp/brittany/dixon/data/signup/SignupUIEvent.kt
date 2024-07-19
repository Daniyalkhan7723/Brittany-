package com.cp.brittany.dixon.data.signup

import com.cp.brittany.dixon.data.login.LoginUIEvent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class SignupUIEvent {

    data class NameChanged(val name: String) : SignupUIEvent()
    data class EmailChanged(val email: String) : SignupUIEvent()
    data class PasswordChanged(val password: String) : SignupUIEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignupUIEvent()
    object RegisterButtonClicked : SignupUIEvent()

    data class GoogleSignInButton(val googleAccountDetails: GoogleSignInAccount, val loginType: Int) :
        SignupUIEvent()

}
