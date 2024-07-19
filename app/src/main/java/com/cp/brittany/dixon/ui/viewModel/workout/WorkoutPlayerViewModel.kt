package com.cp.brittany.dixon.ui.viewModel.workout

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.cp.brittany.dixon.utills.Constants
import com.cp.brittany.dixon.utills.SharePreferenceHelper
import com.cp.brittany.dixon.utills.SocketIO
import com.cp.brittany.dixon.utills.Utils.convertToMilliseconds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutPlayerViewModel @OptIn(UnstableApi::class) @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val applicationContext: Application,
    val player: ExoPlayer,
    val socketIO: SocketIO,
    val preference: SharePreferenceHelper,


    ) : ViewModel() {
    val isPlayerLoading = mutableStateOf(false)

    // Create a data source factory.
    private val dataSourceFactory = DefaultHttpDataSource.Factory()

    var visibleView by mutableStateOf(false)
    var visibleButton by mutableStateOf(true)

    var currentPosition by
    mutableLongStateOf(0)


    var totalDuration by
    mutableLongStateOf(0)


    init {
        isPlayerLoading.value = true
        player.apply {
//            val mediaItem = MediaItem.Builder()
//                .apply {
//                    setUri(videoUrl)
//                    setMediaMetadata(
//                        MediaMetadata.Builder()
//                            .build()
//                    )
//                }
//                .build()
//
//            setMediaItem(mediaItem)
            val source = getProgressiveMediaSource()
            setMediaSource(source)
            prepare()

            if (savedStateHandle.get<Int>(Constants.PROGRESS) == 100) {
                player.seekTo(
                    0
                )
            } else {
                player.seekTo(
                    convertToMilliseconds(
                        savedStateHandle.get<String>(Constants.WATCH_TIME) ?: ""
                    )
                )
            }

            playWhenReady = true
        }
    }

    @OptIn(UnstableApi::class)
    private fun getProgressiveMediaSource(): MediaSource {
        val videoUrl = savedStateHandle.get<String>(Constants.VIDEO_URL)
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
    }


    override fun onCleared() {
        super.onCleared()
        player.apply {
            release()
        }
    }
}