package com.cp.brittany.dixon.ui.screens.auth.forgotPassword

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.cp.brittany.dixon.navigation.navGraphs.forgotPasswordGraph.ForgotPasswordGraph
import com.cp.brittany.dixon.ui.components.SegmentedProgressIndicator
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp

@Composable
fun ForgotPasswordBaseScreen(navController: NavController? = null) {

    var running by remember { mutableFloatStateOf(0f) }
    val maxLimit by remember { mutableFloatStateOf(0.999f) }
    val minLimit by remember { mutableFloatStateOf(0.333f) }
    // Segment progress bar properties
    val progress: Float by animateFloatAsState(
        running, animationSpec = tween(
            durationMillis = 1_000, easing = LinearEasing
        ), label = ""
    )

    LaunchedEffect(key1 = Unit) {
        if (running == 0f) {
            running = 0.333f
        }
    }

    val increaseProgress = remember<() -> Unit> {
        {
            if (running < maxLimit) {
                running += 0.333f
            }
        }
    }

    val decreaseProgress = remember<() -> Unit> {
        {
            if (running > minLimit) {
                running -= 0.333f
            }
        }
    }



    BrittanyDixonTheme {
        Surface(color = MaterialTheme.colorScheme.primary) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SegmentedProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .padding(top = 64.sdp, start = 10.sdp, end = 10.sdp)
                        .fillMaxWidth(),
                )
                ForgotPasswordGraph(
                    onButtonClicked = increaseProgress,
                    onBackPressClicked = decreaseProgress,
                    onBaseBackPress = {
                        navController?.popBackStack()
                    },
                    dismissListener = {
                        navController?.popBackStack()
                    }

                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordBaseScreenPreview() {
    ForgotPasswordBaseScreen()
}