package com.cp.brittany.dixon.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cp.brittany.dixon.navigation.BottomNavigationBar
import com.cp.brittany.dixon.navigation.navGraphs.homeGraph.HomeNavGraph
import com.cp.brittany.dixon.ui.viewModel.home.HomeViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutViewModel

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {

    val categoryWorkoutResponse by homeViewModel.getProfile.collectAsStateWithLifecycle()

    //topBar visibility state
    val topBarVisibilityState = remember {
        mutableStateOf(true)
    }

    val bottomBarVisibilityState = remember {
        mutableStateOf(true)
    }

    val addPaddingOrNot = remember {
        mutableStateOf(false)
    }

    val isFromWorkoutPlayerScreen = remember<(Boolean) -> Unit> {
        {
            addPaddingOrNot.value = it
        }
    }


    val isVisible = remember<(Boolean) -> Unit> {
        { isVisible ->
            topBarVisibilityState.value = isVisible
        }
    }

    val showHideBottomBar = remember<(Boolean) -> Unit> {
        {
            bottomBarVisibilityState.value = it
        }
    }


    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController,
            isVisible = isVisible,
            bottomBarVisibility = bottomBarVisibilityState.value
        )
    }
    ) { padding ->
        Surface(
            modifier = Modifier, color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                modifier = if (addPaddingOrNot.value) {
                    Modifier
                } else {
                    Modifier.padding()
                }
            ) {
                HomeNavGraph(
                    navHostController = navController,
                    onLogout = onLogout,
                    showHideBottomBar = showHideBottomBar,
                    isFromWorkoutPlayerScreen = isFromWorkoutPlayerScreen
                )
            }
        }
    }

}