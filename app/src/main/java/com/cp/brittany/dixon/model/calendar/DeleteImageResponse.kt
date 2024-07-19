package com.cp.brittany.dixon.model.calendar
import androidx.compose.runtime.Immutable

@Immutable
data class DeleteImageResponse(
    var status: Boolean, var code: Int,
    var message: String, var data: Any
)
