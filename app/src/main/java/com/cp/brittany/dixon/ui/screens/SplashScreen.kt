package com.cp.brittany.dixon.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.viewModel.SplashViewModel
import com.cp.brittany.dixon.navigation.navGraphs.Graph
import com.cp.brittany.dixon.navigation.navGraphs.authGraph.AuthRoute
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp
import kotlinx.coroutines.delay

@Composable
inline fun SplashScreen(
    navController: NavController? = null, crossinline navigateToNext: (String) -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()

) {
    val alpha = remember {
        Animatable(0f)
    }
    val coroutineScope = rememberCoroutineScope()

    BrittanyDixonTheme {
        LaunchedEffect(key1 = Unit) {
            alpha.animateTo(
                1f,
                animationSpec = tween(1500)
            )
        }

        LaunchedEffect(key1 = Unit) {
            // will be canceled and re-launched if sth is changed
            delay(2000)
            if (splashViewModel.preference.isUserLoggedIn()) {
                navigateToNext(Graph.HOME)
            } else {
                navigateToNext(AuthRoute.GetStartedScreen.route)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .alpha(alpha.value)
                    .align(Alignment.Center)
                    .width(160.sdp)
                    .height(40.sdp),
                painter = painterResource(id = R.drawable.logo_glampian_splash),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}