package com.cp.brittany.dixon.data.verifyEmail

data class VerifyEmailUIState(
    var otpCode: String = "",

    var otpCodeError: Boolean = false,

    var otpCodeErrorMessage: String = "",

    )
