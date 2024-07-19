package com.cp.brittany.dixon.data.verifyEmail

sealed class VerifyEmailUIEvent {

    data class OtpCodeChanged(val otpCode: String) : VerifyEmailUIEvent()

    object VerifyButtonClicked : VerifyEmailUIEvent()
}
