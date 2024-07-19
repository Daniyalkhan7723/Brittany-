package com.cp.brittany.dixon.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cp.brittany.dixon.navigation.navGraphs.detailGraph.DetailRoute
import com.cp.brittany.dixon.navigation.navGraphs.homeGraph.HomeScreenRoute
import com.cp.brittany.dixon.ui.theme.AppFont
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun BottomNavigationBar(
    navController: NavController,
    isVisible: (Boolean) -> Unit,
    bottomBarVisibility: Boolean
) {
    val navItemList = listOf(
        BottomNavItem.Workout,
        BottomNavItem.InsightsNav,
        BottomNavItem.FavouriteNav,
        BottomNavItem.CalendarNav,
        BottomNavItem.ProfileNav,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var bottomNavVisibility by remember { mutableStateOf(true) }

    if (bottomNavVisibility) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            navItemList.forEachIndexed { _, screen ->
                NavigationBarItem(
                    selected = navBackStackEntry?.destination?.route == screen.route,
                    icon = {
//                        if (index == 0)
//                            Icons(
//                                navBackStackEntry,
//                                screen,
//                                modifier = Modifier.padding(start = 10.sdp)
//                            )
//                        else Icons(navBackStackEntry, screen)
                        Icons(navBackStackEntry, screen)
                    },

                    label = {
                        BottomBarText(screen)
                    },
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
//                    modifier = Modifier.padding(top = 5.sdp, bottom = 10.sdp),
                    colors = NavigationBarItemDefaults
                        .colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            indicatorColor = Color.Transparent,
                            selectedTextColor = MaterialTheme.colorScheme.secondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                )

            }
        }
    }

    //hide topBar
    when (navBackStackEntry?.destination?.route) {
        HomeScreenRoute.WorkoutScreen.route -> {
            bottomNavVisibility = bottomBarVisibility
            isVisible(bottomBarVisibility)
        }

        DetailRoute.WorkoutDetail.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.ScheduleWorkoutsDetails.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.InsightDetails.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.WorkoutPlayer.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

//        DetailRoute.WorkoutSeeMore.route -> {
//            bottomNavVisibility = false
//            isVisible(false)
//        }

        DetailRoute.WorkoutAllSearch.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.AllScheduledWorkouts.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }
        DetailRoute.SubscriptionPlan.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.SubscriptionDetail.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.AllInsights.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }


        DetailRoute.ProfileDetail.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.ProfileEdit.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.ResetPassword.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.SubscriptionInfo.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        DetailRoute.ImagePreview.route -> {
            bottomNavVisibility = false
            isVisible(false)
        }

        else -> {
            bottomNavVisibility = true
            isVisible(false)
        }
    }
}

@Composable
private fun BottomBarText(screen: BottomNavItem, modifier: Modifier = Modifier) {
    Text(
        text = screen.tittle,
        modifier = modifier,
        style = TextStyle(
            fontSize = 11.ssp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = AppFont.MyCustomFont
        )
    )
}

@Composable
private fun Icons(
    navBackStackEntry: NavBackStackEntry?,
    screen: BottomNavItem,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .padding(bottom = 2.sdp)
            .size(20.sdp),
        painter = if (navBackStackEntry?.destination?.route == screen.route) painterResource(
            id = screen.icon_on
        ) else painterResource(id = screen.icon_off),
        contentDescription = null,
    )
}

