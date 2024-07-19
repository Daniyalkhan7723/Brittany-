package com.cp.brittany.dixon.data.profileSettings

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class ProfileSettingUIState(
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var googleAccountDetails: GoogleSignInAccount? = null
)
