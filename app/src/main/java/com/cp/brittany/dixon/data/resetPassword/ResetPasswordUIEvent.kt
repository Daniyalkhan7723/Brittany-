package com.cp.brittany.dixon.data.resetPassword


sealed class ResetPasswordUIEvent {
    data class OldPasswordChanged(val oldPassword: String) : ResetPasswordUIEvent()
    data class NewPasswordChanged(val newPassword: String) : ResetPasswordUIEvent()
    data class ConfirmNewPasswordChanged(val confirmNewPassword: String) : ResetPasswordUIEvent()

    object NextButtonClicked : ResetPasswordUIEvent()
}
