package com.example.pong_extreme

import kotlin.random.Random

class PowerupManager {
    enum class PowerUpType{
        BIGPADDLE, SMALLPADDLE, EXTRABALL, STICKYPAD, SLOWMOTION
    }

    fun shouldHavePowerup(): Boolean{
        val randomize = Random.nextInt(1,101)
        return randomize <= 20
    }
}