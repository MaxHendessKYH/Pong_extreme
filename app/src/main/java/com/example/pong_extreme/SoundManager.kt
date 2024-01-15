package com.example.pong_extreme
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
class SoundManager(private val context: Context) {
    var soundPool: SoundPool
    private var ballHitPaddleSound: Int = 0
    private var ballHitBrickSound: Int = 0
    private var gameOverSound: Int = 0
    private var powerupSound: Int = 0
    // Initializing the SoundPool and loading the sound file
    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        // Building the SoundPool instance
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
        // Loading the sound file into the SoundPool
        ballHitPaddleSound = soundPool.load(context, R.raw.paddle,1)
        ballHitBrickSound = soundPool.load(context, R.raw.brick, 1)
        gameOverSound = soundPool.load(context, R.raw.gameoversound2, 1)
        powerupSound = soundPool.load(context, R.raw.powerup, 1)
        // To load more sound files for example ball collision with bricks implement here
    }
    // Releasing resources when they are no longer needed
    fun release() {
        soundPool.release()
    }
    // Playing the loaded sound/sounds
    fun playSoundPaddle() {
        soundPool.play(ballHitPaddleSound, 1.0f, 1.0f, 1, 0, 1.0f)
    }
    fun playSoundBrick() {
        soundPool.play(ballHitBrickSound, 1.0f, 1.0f, 1, 0, 1.0f)
    }
    fun playSoundGameOver(){
        soundPool.play(gameOverSound, 1.0f,1.0f,1,0,1.0f)
    }
    fun playSoundPowerup(){
        soundPool.play(powerupSound, 1.0f,1.0f,1,0,1.0f)
    }
}




