package com.cp.brittany.dixon.model.auth

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var email_verified_at: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var avatar: String? = null,
    var timezone: String? = null,
    var is_verified_email: Boolean? = null,
    var is_premium: Boolean? = null,
    var token: String? = null,

    )


