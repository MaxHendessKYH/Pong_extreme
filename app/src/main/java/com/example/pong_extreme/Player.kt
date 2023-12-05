package com.example.pong_extreme

import android.graphics.BitmapFactory

class Player() {

    private var lives: Int = 3
    private var score: Int = 0

    fun reduceLife() {
        lives -= 1
    }
    fun showLives(): Int {
        return lives
    }

    fun getScore(): Int {
        return score
    }
    fun increaseScore(points: Int) {
       score += points
    }
}