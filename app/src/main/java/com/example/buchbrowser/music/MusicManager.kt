package com.example.buchbrowser.music

import android.content.Context
import android.media.MediaPlayer
import com.example.buchbrowser.R

object MusicManager {

    private var mediaPlayer: MediaPlayer? = null

    private var currentTheme = MusicTheme.NONE

    fun playTheme(
        context: Context,
        theme: MusicTheme
    ) {

        // Läuft das gleiche Theme bereits?
        if (theme == currentTheme && mediaPlayer?.isPlaying == true) {
            return
        }

        stop()

        // Wenn "Keine Musik" gewählt wurde,
        // soll gar nichts abgespielt werden.
        if (theme == MusicTheme.NONE) {
            currentTheme = MusicTheme.NONE
            return
        }

        val music = when (theme) {

            MusicTheme.NONE -> return

            MusicTheme.HARRY -> R.raw.harry

            MusicTheme.HERR -> R.raw.herr

            MusicTheme.GAME -> R.raw.game

            MusicTheme.PANEM -> R.raw.panem
        }

        mediaPlayer = MediaPlayer.create(context, music)

        mediaPlayer?.apply {
            isLooping = true
            start()
        }

        currentTheme = theme
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentTheme = MusicTheme.NONE
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}