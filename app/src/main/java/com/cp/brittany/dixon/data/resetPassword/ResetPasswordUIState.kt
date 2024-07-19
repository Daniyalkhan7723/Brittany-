package com.cp.brittany.dixon.data.resetPassword

data class ResetPasswordUIState(
    var oldPassword: String = "",
    var oldPasswordError: Boolean = false,
    var oldPasswordErrorMessage: String = "",

    var newPassword: String = "",
    var newPasswordError: Boolean = false,
    var newPasswordErrorMessage: String = "",

    var confirmNewPassword: String = "",
    var confirmNewPasswordError: Boolean = false,
    var confirmNewPasswordErrorMessage: String = "",
)

