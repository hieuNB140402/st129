package com.example.st129_gravityfalls_maker.extionsion

import android.media.MediaPlayer

fun safeSetVolume(mediaPlayer: MediaPlayer?, volume: Float) {
    if (mediaPlayer != null && !mediaPlayer.isReleased()) {
        mediaPlayer.setVolume(volume, volume)
    }
}

fun releaseMediaPlayer(mediaPlayer: MediaPlayer?) {
    if (mediaPlayer != null && !mediaPlayer.isReleased()) {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}

fun MediaPlayer?.isReleased(): Boolean {
    return try {
        this?.isPlaying
        false
    } catch (e: IllegalStateException) {
        true
    }
}