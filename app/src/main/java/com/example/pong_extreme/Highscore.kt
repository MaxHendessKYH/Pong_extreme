package com.example.pong_extreme

import java.io.Serializable

class Highscore(var name: String, var score: Int, var gameMode: String) {
    fun getScoreToString(): String
    {
        return score.toString()
    }
}