package com.cp.brittany.dixon.ui.screens.home.workout

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.cp.brittany.dixon.ui.components.DisposableEffectWithLifecycle
import com.cp.brittany.dixon.ui.components.LockScreenOrientation
import com.cp.brittany.dixon.ui.screens.home.workout.workoutComposables.VideoLayoutComposable
import com.cp.brittany.dixon.ui.viewModel.SharedViewModel
import com.cp.brittany.dixon.ui.viewModel.workout.WorkoutPlayerViewModel
import com.cp.brittany.dixon.utills.Utils
import com.cp.brittany.dixon.utills.sdp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject


@OptIn(UnstableApi::class)
@Composable
fun WorkoutPlayerScreen(
    workoutPlayerViewModel: WorkoutPlayerViewModel = hiltViewModel(),
    isFromWorkoutPlayerScreen: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel,
    onBackPress: () -> Unit,
    workoutId: String
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    var isPlaying by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        isFromWorkoutPlayerScreen(true)
        if (!workoutPlayerViewModel.socketIO.isConnected()!!) {
            workoutPlayerViewModel.socketIO.connect()
        }
    }

    DisposableEffectWithLifecycle(
        onResume = {

        }
    )

    val onBackPress = remember {
        {
            onBackPress()
            sharedViewModel.setBackNavigationFromPlayer(true)
        }
    }

    val onPause = remember {
        {
            workoutPlayerViewModel.player.pause()
        }
    }

    val onPlay = remember {
        {
            workoutPlayerViewModel.player.play()
        }
    }


    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlayingValue: Boolean) {
            isPlaying = isPlayingValue
            super.onIsPlayingChanged(isPlayingValue)
        }

        override fun onEvents(
            player: Player, events: Player.Events
        ) {
            super.onEvents(player, events)
            coroutineScope.launch {
                delay(5000)
                Log.d(
                    "ccdcdcdcdccscs", Utils.formatTime(
                        workoutPlayerViewModel.player.currentPosition
                    )
                )
                try {
                    val obj = JSONObject().apply {
                        put("user_id", workoutPlayerViewModel.preference.getUser()?.id)
                        put("workout_id", workoutId)
                        put(
                            "watched_time", Utils.formatTime(
                                workoutPlayerViewModel.player.currentPosition
                            )
                        )
                    }
                    Log.e("updateWatchTime", obj.toString())
                    workoutPlayerViewModel.socketIO.emit("updateWatchTime", obj)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }


        }


        override fun onMediaItemTransition(
            mediaItem: MediaItem?, reason: Int
        ) {
            super.onMediaItemTransition(
                mediaItem, reason
            )
            // everytime the media item changes show the title
        }

        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_IDLE -> {

                }

                Player.STATE_BUFFERING -> {
                    workoutPlayerViewModel.isPlayerLoading.value = true
                }

                Player.STATE_READY -> {
                    workoutPlayerViewModel.isPlayerLoading.value = false
                }

                Player.STATE_ENDED -> {
                    workoutPlayerViewModel.isPlayerLoading.value = false
                }
            }
        }
    }


    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            sharedViewModel.refreshWorkoutDetails(true)
            workoutPlayerViewModel.player.removeListener(listener)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

//    DisposableEffect(lifecycleOnDestroy) {
//        onDispose {
//            if (workoutPlayerViewModel.socketIO.isConnected()!!) {
//                workoutPlayerViewModel.socketIO.disconnect()
//            }
//        }
//    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PlayerView(context).apply {
                        player = workoutPlayerViewModel.player
//                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        workoutPlayerViewModel.player.addListener(listener)
                        useController = false
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                },
                update = {
                    when (lifecycle) {
                        Lifecycle.Event.ON_PAUSE -> {
                            it.onPause()
                            it.player?.pause()
                        }

                        Lifecycle.Event.ON_RESUME -> {
                            it.onResume()
                            it.player?.play()
                        }

                        else -> Unit
                    }
                },
            )

            VideoLayoutComposable(
                isPlaying = isPlaying, onPlay = onPlay, onPause = onPause, onBackPress = onBackPress
            )
            if (workoutPlayerViewModel.isPlayerLoading.value) {
                CircularProgressIndicator(
                    strokeWidth = 3.sdp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(30.sdp)
                        .align(Alignment.Center)
                )
            }

        }

    }

}
