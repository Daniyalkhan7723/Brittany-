package com.cp.brittany.dixon.navigation.navGraphs.homeGraph

sealed class HomeScreenRoute(val route: String) {

    object WorkoutScreen : HomeScreenRoute("workout_screen")
    object InsightScreen : HomeScreenRoute("insight_screen")
    object FavoritesScreen : HomeScreenRoute("favourite_screen")
    object CalendarScreen : HomeScreenRoute("calendar_screen")
    object ProfileScreen : HomeScreenRoute("profile_screen")
}
