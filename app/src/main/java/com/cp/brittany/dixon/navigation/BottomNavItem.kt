package com.cp.brittany.dixon.navigation

import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.navigation.navGraphs.homeGraph.HomeScreenRoute


sealed class BottomNavItem(
    val tittle: String,
    val icon_off: Int,
    val route: String,
    val icon_on: Int
) {
    object Workout : BottomNavItem(
        tittle = "Workout",
        icon_on = R.drawable.ic_weight_selected,
        route = HomeScreenRoute.WorkoutScreen.route,
        icon_off = R.drawable.ic_weight
    )

    object InsightsNav : BottomNavItem(
        tittle = "Insights",
        icon_on = R.drawable.ic_book_selected,
        route = HomeScreenRoute.InsightScreen.route,
        icon_off = R.drawable.ic_book
    )

    object FavouriteNav : BottomNavItem(
        tittle = "Favorites",
        icon_on = R.drawable.ic_star_selected,
        route = HomeScreenRoute.FavoritesScreen.route,
        icon_off = R.drawable.ic_star
    )

    object CalendarNav : BottomNavItem(
        tittle = "Calendar",
        icon_on = R.drawable.ic_calendar_selected,
        route = HomeScreenRoute.CalendarScreen.route,
        icon_off = R.drawable.ic_calendar_unselected
    )

    object ProfileNav : BottomNavItem(
        tittle = "Profile",
        icon_on = R.drawable.ic_frame_selected,
        route = HomeScreenRoute.ProfileScreen.route,
        icon_off = R.drawable.profile
    )
}
