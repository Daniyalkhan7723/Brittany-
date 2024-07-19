package com.cp.brittany.dixon.data.forgotPassword.addEmail

data class ForgotPasswordUIState(
    var email: String = "",
    var emailError: Boolean = false,
    var emailErrorMessage: String = "",
)

