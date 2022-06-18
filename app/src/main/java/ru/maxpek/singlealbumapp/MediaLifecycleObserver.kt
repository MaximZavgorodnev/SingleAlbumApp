package ru.maxpek.singlealbumapp

import android.media.MediaPlayer
import androidx.lifecycle.*

class MediaLifecycleObserver : LifecycleEventObserver {
    var player: MediaPlayer? = MediaPlayer()
    var totalTime: Int = 0

    fun play(){
        player?.setOnPreparedListener {
            it.start()
        }
        player?.isLooping = false
        player?.prepare()
    }

    fun whatTotalTime(): Int {
        totalTime = player!!.duration
        return player!!.duration
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> player?.pause()
            Lifecycle.Event.ON_STOP -> {
                player?.reset()
            }
            Lifecycle.Event.ON_DESTROY ->
                source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}