package com.cp.brittany.dixon.data.editProfile

data class EditProfileUIState(
    var fullName: String = "",
    var fullNameError: Boolean = false,
    var fullNameErrorMessage: String = "",
    var image: String = "",
)
