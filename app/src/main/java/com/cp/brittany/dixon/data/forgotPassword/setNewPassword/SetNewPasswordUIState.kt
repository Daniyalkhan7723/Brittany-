package com.cp.brittany.dixon.data.forgotPassword.setNewPassword

data class SetNewPasswordUIState(
    var password: String = "",
    var passwordError: Boolean = false,
    var passwordErrorMessage: String = "",

    var confirmPassword: String = "",
    var confirmPasswordError: Boolean = false,
    var confirmPasswordErrorMessage: String = "",
)

