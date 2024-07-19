package com.cp.brittany.dixon.ui.screens.auth.forgotPassword

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.AuthHeaderComponent
import com.cp.brittany.dixon.ui.components.ButtonComponent
import com.cp.brittany.dixon.ui.modifiers.bounceClick
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import com.cp.brittany.dixon.utills.sdp
import kotlinx.coroutines.launch

@Composable
inline fun PasswordResetSuccessfullyScreen(
    crossinline dismissListener: () -> Unit
) {
    val alpha = remember {
        Animatable(0f)
    }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(250f) }

    LaunchedEffect(key1 = Unit) {
        alpha.animateTo(
            1f,
            animationSpec = tween(1000)
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
            modifier = Modifier
                .fillMaxWidth()
                .height(330.sdp),
            color = MaterialTheme.colorScheme.secondary
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.sdp),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(topStart = 30.sdp, topEnd = 30.sdp)
            ) {
                Box {
                    Column( // or whatever your parent composable is
                        modifier = Modifier
                            .alpha(alpha.value)
//                            .padding(bottom = 10.sdp)
                            .offset {
                                IntOffset(
                                    offsetX.value.toInt(),
                                    offsetY.value.toInt()
                                )
                            }
                    ) {
                        AuthHeaderComponent(
                            stringResource(id = R.string.password_reset_successfully),
                            stringResource(id = R.string.you_have_successfully),
                            R.drawable.logo_lock,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.sdp),
                            textModifier = Modifier
                                .padding(horizontal = 15.sdp)
                        )

                    }

                    ButtonComponent(
                        value = stringResource(id = R.string.lets_go),
                        onButtonClicked = {
                            dismissListener.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                            .heightIn(40.sdp)
                            .padding(bottom = 40.sdp, start = 15.sdp, end = 15.sdp, top = 30.sdp),
                        buttonColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.background
                    )
                }

            }

        }

    }
}

