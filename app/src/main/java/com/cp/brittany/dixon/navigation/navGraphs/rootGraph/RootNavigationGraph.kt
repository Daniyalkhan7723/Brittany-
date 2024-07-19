package com.cp.brittany.dixon.navigation.navGraphs.rootGraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.navigation.navGraphs.authGraph.authNavGraph
import com.cp.brittany.dixon.navigation.navGraphs.composable
import com.cp.brittany.dixon.ui.screens.home.HomeScreen

@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION,
    ) {
        authNavGraph(navHostController)

        composable(route = Graph.HOME) {
            val onLogout = remember {
                {
                    navHostController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.HOME) {
                            inclusive = true
                        }
                    }
                }

            }
            HomeScreen(onLogout = onLogout)
        }
    }
}