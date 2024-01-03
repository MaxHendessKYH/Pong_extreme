package com.example.pong_extreme
import kotlin.random.Random

class PowerupManager {
    enum class PowerUpType{
        BIGPADDLE, SMALLPADDLE,STICKY
    }
    var activePower = "None"
    var powerupActive: Boolean = false
    var stickyTimer = 30
    fun shouldHavePowerup(): Boolean{
        val randomize = Random.nextInt(1,101)
        return randomize <= 100
    }
    fun setSticky(paddle: Paddle)
    {
        if(!paddle.isSticky)
            paddle.isSticky = true
        else if(paddle.isSticky)
            paddle.isSticky = false
    }
    fun stickyPaddleReleaseCountdown(paddle: Paddle)
    {
            stickyTimer--
            if (stickyTimer == 0) {
                setSticky(paddle)
                stickyTimer = 30
            }
    }
}