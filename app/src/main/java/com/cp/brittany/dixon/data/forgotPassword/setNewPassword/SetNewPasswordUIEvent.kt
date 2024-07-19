package com.cp.brittany.dixon.data.forgotPassword.setNewPassword


sealed class SetNewPasswordUIEvent {
    data class PasswordChanged(val password: String) : SetNewPasswordUIEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SetNewPasswordUIEvent()

    object NextButtonClicked : SetNewPasswordUIEvent()
}
