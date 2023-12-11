package com.example.pong_extreme

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool

class SoundManager(private val context: Context) {
    var soundPool: SoundPool
    private var ballHitPaddleSound: Int = 0

    // Initializing the SoundPool and loading the sound file
    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()


        // Building the SoundPool instance
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        // Loading the sound file into the SoundPool
        ballHitPaddleSound = soundPool.load(context, R.raw.beam,1)
        // To load more sound files for example ball collision with bricks implement here

    }

    // Releasing resources when they are no longer needed
    fun release() {
        soundPool.release()
    }

    // Playing the loaded sound/sounds
    fun playSound() {
        soundPool.play(ballHitPaddleSound, 1.0f, 1.0f, 1, 0, 1.0f)
    }


}




