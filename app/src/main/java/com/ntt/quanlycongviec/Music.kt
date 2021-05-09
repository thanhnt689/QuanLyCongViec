package com.ntt.quanlycongviec

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder


class Music : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.start()
        return START_NOT_STICKY
    }
}