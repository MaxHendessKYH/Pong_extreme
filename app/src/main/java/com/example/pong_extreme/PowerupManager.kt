package com.example.pong_extreme
import kotlin.random.Random

class PowerupManager {
    enum class PowerUpType{
        BIGPADDLE, SMALLPADDLE,STICKY, SLOWMOTION
    }

    var activePower = "None"
    var powerupActive: Boolean = false
    var stickyTimer = 30

    fun triggerPowerUp() : PowerUpType? {
        if(shouldHavePowerup()){
            val randomNumber = Random.nextInt(1,5)

            if(randomNumber == 1){
                return PowerUpType.SLOWMOTION
            }
        }
        return null
    }


    fun shouldHavePowerup(): Boolean{
        val randomize = Random.nextInt(1,101)
        return randomize <= 20
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