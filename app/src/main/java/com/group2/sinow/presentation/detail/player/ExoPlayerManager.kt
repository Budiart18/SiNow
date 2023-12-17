package com.group2.sinow.presentation.detail.player

import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlin.math.max

class ExoPlayerManager(private val playerView: PlayerView) : PlayerManager {

    private var player: ExoPlayer? = null

    private var startAutoPlay = false
    private var startItemIndex = 0
    private var startPosition : Long = 0

    private var currentMediaItem: MediaItem? = null

    private fun play(mediaItem: MediaItem?, haveStartPosition: Boolean = false) {
        currentMediaItem = mediaItem
        currentMediaItem?.let {
            player?.playWhenReady = startAutoPlay
            player?.setMediaItem(it, !haveStartPosition)
            player?.prepare()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(playerView.context)
            .build()
            .also { exoPlayer ->
                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()
                playerView.player = exoPlayer
            }
        val haveStartPosition = startItemIndex != C.INDEX_UNSET
        if (haveStartPosition) {
            player?.seekTo(startItemIndex, startPosition)
        }
        play(currentMediaItem, haveStartPosition)
    }

    override fun play(videoUrl: String) {
        play(MediaItem.Builder().setUri(videoUrl).build(), false)
    }

    private fun updateIndex() {
        player?.let {
            startAutoPlay = it.playWhenReady
            startItemIndex = it.currentMediaItemIndex
            startPosition = max(0, it.contentPosition)
        }
    }

    private fun clearIndex() {
        startAutoPlay = true
        startItemIndex = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            updateIndex()
            exoPlayer.release()
            player = null
            playerView.player = null
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        clearIndex()
        //setClickListener()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer()
            playerView.onResume()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        if (Build.VERSION.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (Build.VERSION.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (Build.VERSION.SDK_INT <= 23 || player == null) {
            initializePlayer()
            playerView.onResume()
        }
    }

}