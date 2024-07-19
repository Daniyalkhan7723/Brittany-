package com.cp.brittany.dixon.data.forgotPassword.confirmYourEmail

sealed class ConfirmYourEmailUIEvent {

    data class OtpCodeChanged(val otpCode: String) : ConfirmYourEmailUIEvent()

    object NextButtonClicked : ConfirmYourEmailUIEvent()
}
