package com.cp.brittany.dixon.data.signup

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class SignUpUIState(
    var fullName: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var googleAccountDetails: GoogleSignInAccount? = null,
    var loginType: Int = 0,


    var fullNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,


    var fullNameErrorMessage: String = "",
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var confirmPasswordErrorMessage: String = "",


    )
