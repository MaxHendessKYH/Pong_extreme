package com.example.pong_extreme

class Player(var gameMode: String) {

    private var lives: Int = 1000
    private var score: Int = 0

    fun reduceLife() {
        lives -= 1
    }

    fun increaseLife() {
        lives += 1
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