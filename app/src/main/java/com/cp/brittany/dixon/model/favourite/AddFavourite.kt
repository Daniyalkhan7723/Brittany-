package com.cp.brittany.dixon.model.favourite

import androidx.compose.runtime.Immutable

@Immutable
data class AddFavourite(
    val `data`: Data,
    val message: String,
    val pagination: Any,
    val status: Boolean,
    val statusCode: Int
)
@Immutable

data class Data(
    val attached: List<Int>,
    val detached: List<Any>,
    val is_favorite: Boolean
)