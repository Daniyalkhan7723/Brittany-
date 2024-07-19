package com.cp.brittany.dixon.data.forgotPassword.addEmail


sealed class ForgotPasswordUIEvent {

    data class EmailChanged(val email: String) : ForgotPasswordUIEvent()

    object NextButtonClicked : ForgotPasswordUIEvent()
}
