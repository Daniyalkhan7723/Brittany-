package com.cp.brittany.dixon.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.BorderButtonRound
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.components.HeadingTextComponent
import com.cp.brittany.dixon.ui.components.NormalTextComponent
import com.cp.brittany.dixon.navigation.navGraphs.authGraph.AuthRoute
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp


@Composable
fun GetStartedScreen(navController: NavController? = null) {

    val paddingValues = PaddingValues(
        horizontal = 20.sdp,
        vertical = 12.sdp
    )
    val context = LocalContext.current.applicationContext

    val alpha = remember {
        Animatable(0.1f)
    }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(200f) }

    LaunchedEffect(key1 = Unit) {
        alpha.animateTo(
            1.0f,
            animationSpec = tween(1500)
        )
    }

    LaunchedEffect(key1 = Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }

    BrittanyDixonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.background_get_started),
                    contentDescription = "getStartedImage",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
            }
            Column(
                modifier = Modifier
                    .alpha(alpha.value)
                    .offset {
                        IntOffset(
                            offsetX.value.toInt(),
                            offsetY.value.toInt()
                        )
                    }
                    .padding(
                        bottom = 60.sdp,
                    ),
                verticalArrangement = Arrangement.Bottom,

                ) {

                HeadingTextComponent(
                    value = stringResource(id = R.string.the_ultimated_15_mint_fitness_app),
                    textSize = 26.ssp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.sdp)
                        .heightIn(),
                )

                Spacer(modifier = Modifier.height(15.sdp))
                NormalTextComponent(
                    value = stringResource(id = R.string.quick_and_effective_workout_design),
                    modifier = Modifier
                        .padding(horizontal = 10.sdp)
                        .heightIn(min = 40.sdp),
                    textSize = 12.ssp,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(15.sdp))

                Column(
                    modifier = Modifier.padding(
                        start = 20.sdp,
                        end = 20.sdp
                    )
                ) {
                    ButtonComponent(
                        value = stringResource(id = R.string.get_started),
                        onButtonClicked = {
                            navController?.navigate(AuthRoute.SignUpScreen.route)
                        },
                        buttonColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.background,
                    )
                    Spacer(modifier = Modifier.height(15.sdp))


                    BorderButtonRound(
                        value = stringResource(id = R.string.login),
                        onButtonClicked = {
                            navController?.navigate(AuthRoute.LoginScreen.route)
                        },
                        paddingValues = paddingValues,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shapeRadius = 48,
                        borderRadius = 50
//                            .bounceClick()
                    )
                }


            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun GetStartedPreview() {
    GetStartedScreen()
}