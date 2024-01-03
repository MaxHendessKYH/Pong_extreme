package com.example.pong_extreme

import kotlin.random.Random

class PowerupManager {
    enum class PowerUpType{
        BIGPADDLE, SMALLPADDLE, EXTRABALL, STICKYPAD, SLOWMOTION
    }

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
}