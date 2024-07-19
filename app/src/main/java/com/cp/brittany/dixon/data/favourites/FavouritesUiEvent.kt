package com.cp.brittany.dixon.data.favourites


sealed class FavouritesUiEvent {
    data class AddToFavourites(val id: Int) : FavouritesUiEvent()
    object GetFavouriteData : FavouritesUiEvent()
}
