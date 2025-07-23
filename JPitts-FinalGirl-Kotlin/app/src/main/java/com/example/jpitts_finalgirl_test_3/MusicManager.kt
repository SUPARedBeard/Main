package com.example.jpitts_finalgirl_test_3.util

import android.content.Context
import android.media.MediaPlayer
import com.example.jpitts_finalgirl_test_3.R

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null

    fun startBackgroundMusic(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    fun stopBackgroundMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
