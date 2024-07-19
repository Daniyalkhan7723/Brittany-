package com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables

import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cp.brittany.dixon.R
import com.cp.brittany.dixon.ui.components.HeadingTextComponentWithoutFullWidth
import com.cp.brittany.dixon.ui.components.findActivity
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutPlayerViewModel
import com.cp.brittany.dixon.utills.Utils.formatTime
import com.cp.brittany.dixon.utills.convertToText
import com.cp.brittany.dixon.utills.sdp
import com.cp.brittany.dixon.utills.ssp
import kotlinx.coroutines.delay

@Composable
fun VideoLayoutComposable(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    workoutPlayerViewModel: WorkoutPlayerViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {

    workoutPlayerViewModel.apply {
        val context = LocalContext.current
        val activity = LocalContext.current.findActivity()

//        val currentPosition = remember {
//            mutableLongStateOf(0)
//        }
//
//
//        val totalDuration = remember {
//            mutableLongStateOf(0)
//        }

        LaunchedEffect(player.duration) {
            if (player.duration > 0) {
                totalDuration = player.duration
            }
        }


        val onBackPressedCallback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPress()
                }
            }
        }
        val onBackPressedDispatcher = activity?.onBackPressedDispatcher
        DisposableEffect(onBackPressedDispatcher) {
            onBackPressedDispatcher?.addCallback(onBackPressedCallback)
            onDispose { onBackPressedCallback.remove() }
        }


        LaunchedEffect(
            key1 = player.currentPosition,
            key2 = player.isPlaying
        ) {
            delay(1000)
            currentPosition = player.currentPosition
        }

        val remainTime = totalDuration - currentPosition


        val playPauseClick by remember {
            mutableStateOf(Modifier.clickable {
                visibleButton = !visibleButton
                if (isPlaying) {
                    onPause()
                } else {
                    onPlay()
                }
            })
        }

        val click by remember {
            mutableStateOf(Modifier.clickable {
                visibleView = !visibleView
            })
        }
        val onBackButtonClick = remember {
            {
                onBackPress()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(click)
        ) {
            AnimatedVisibility(
                visible = visibleView,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .matchParentSize()
                        .background(
                            Color.Black
                                .copy(alpha = 0.6f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
//                      .then(playPauseClick)
                            .clickable {
                                visibleButton = !visibleButton
                                if (isPlaying) {
                                    onPause()
                                } else {
                                    onPlay()
                                }
                            },
                        painter =
                        if (visibleButton) {
                            painterResource(
                                id = R.drawable.ic_pause
                            )
                        } else {
                            painterResource(
                                id = R.drawable.ic_play
                            )
                        },
                        contentDescription = null,
                        tint = Color.White
                    )

                }

            }

            if (!isPlayerLoading.value) {
                Box(
                    modifier = with(
                        Modifier
                            .align(Alignment.BottomStart)
                            .height(125.sdp)
                            .width(185.sdp)
                            .padding(start = 20.sdp, bottom = 20.sdp)
                    )
                    {
                        paint(
                            // Replace with your image id
                            painterResource(id = R.drawable.bg_timer),
                        )

                    })
                {
                    Column(modifier = Modifier.padding(start = 20.sdp)) {
                        Spacer(modifier = Modifier.height(15.sdp))
                        Image(
                            modifier = Modifier
                                .size(35.sdp),
                            painter = painterResource(id = R.drawable.ic_timer),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )

                        Spacer(modifier = Modifier.height(10.sdp))

                        HeadingTextComponentWithoutFullWidth(
                            value = if (remainTime >= 0) remainTime.convertToText() + " min" else "",
                            textSize = 12.ssp,
                            MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.heightIn(),

                            )
                        Spacer(modifier = Modifier.height(2.sdp))


                        HeadingTextComponentWithoutFullWidth(
                            value = stringResource(R.string.remaining_time),
                            textSize = 12.ssp,
                            MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.heightIn()
                        )
                    }

                }
            }

            Card(
                shape = RoundedCornerShape(30.sdp),
                onClick = onBackButtonClick,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                        .copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 15.sdp, start = 20.sdp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(6.sdp)
                        .size(22.sdp),
                )
            }

        }
    }
}