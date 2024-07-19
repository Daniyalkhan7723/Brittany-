package com.cp.brittany.dixon.data.editProfile


sealed class EditProfileEvent {
    data class NameChanged(val name: String) : EditProfileEvent()
    object EditProfileButtonClicked : EditProfileEvent()
    data class GetImageButtonClicked(val image: String?) : EditProfileEvent()

}
