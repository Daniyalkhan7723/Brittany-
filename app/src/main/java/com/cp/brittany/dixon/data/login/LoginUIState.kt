package com.cp.brittany.dixon.data.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class LoginUIState(
    var email: String = "",
    var password: String = "",

    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var loginType: Int = 0,


    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var googleAccountDetails: GoogleSignInAccount? = null


)
