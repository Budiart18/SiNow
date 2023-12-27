package com.group2.sinow.presentation.detail.player

import androidx.lifecycle.DefaultLifecycleObserver

interface PlayerManager : DefaultLifecycleObserver {
    fun play(videoUrl : String, onFullScreenListener: (Boolean) -> Unit)

    fun release()
}
