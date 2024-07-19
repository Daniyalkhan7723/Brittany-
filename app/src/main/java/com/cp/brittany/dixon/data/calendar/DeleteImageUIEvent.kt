package com.cp.brittany.dixon.data.calendar

sealed class DeleteImageUIEvent {
    data class DeleteImage(val imageId: String) :
        DeleteImageUIEvent()

}
