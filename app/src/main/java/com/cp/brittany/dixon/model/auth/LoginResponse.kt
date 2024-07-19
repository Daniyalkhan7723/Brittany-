package com.cp.brittany.dixon.model.auth

import androidx.compose.runtime.Immutable

@Immutable
data class LoginResponse(
    var status: Boolean, var code: Int,
    var message: String, var data: User
)

