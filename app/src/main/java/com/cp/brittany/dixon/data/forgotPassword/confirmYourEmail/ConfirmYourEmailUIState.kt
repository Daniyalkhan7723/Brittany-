package com.cp.brittany.dixon.data.forgotPassword.confirmYourEmail

data class ConfirmYourEmailUIState(
    var otpCode: String = "",

    var otpCodeError: Boolean = false,

    var otpCodeErrorMessage: String = "",

    )
