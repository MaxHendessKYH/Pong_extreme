package com.example.pong_extreme
import kotlin.random.Random

class PowerupManager {
    enum class PowerUpType{
        BIGPADDLE, SMALLPADDLE, EXTRABALL, STICKYPAD, SLOWMOTION
    }

    var stickyTimer = 30
    fun shouldHavePowerup(): Boolean{
        val randomize = Random.nextInt(1,101)
        return randomize <= 100
    }
    fun checkForPowerup(paddle: Paddle)
    {
        if(shouldHavePowerup())
        {
            //Apply powerups in when
            var random = Random.nextInt(1, 5)
            when(random)
            {
                1 -> setSticky(paddle)
            }
        }
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
        if(stickyTimer == 0)
        {
            setSticky(paddle)
            stickyTimer = 30
        }
    }
}