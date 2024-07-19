package com.cp.brittany.dixon.data.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class LoginUIEvent {

    data class EmailChanged(val email: String) : LoginUIEvent()
    data class PasswordChanged(val password: String) : LoginUIEvent()
    data class GoogleSignInButton(val googleAccountDetails: GoogleSignInAccount, val loginType: Int) :
        LoginUIEvent()

    object LoginButtonClicked : LoginUIEvent()
}
 