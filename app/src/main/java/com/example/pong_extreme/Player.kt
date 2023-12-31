package com.example.pong_extreme
import android.graphics.BitmapFactory

class Player(var gameMode: String ) {

    private var lives: Int = 1000
    private var score: Int = 0
    private var levelComplete: Boolean = false
    fun reduceLife() {
        lives -= 1
    }
    fun showLives(): Int {
        return lives
    }
    fun setLevelComplete(setBool : Boolean)
    {
        levelComplete = setBool
    }
    fun getLevelComplete(): Boolean
    {
        return levelComplete
    }
    fun getScore(): Int {
        return score
    }
    fun increaseScore(points: Int) {
       score += points
    }
}