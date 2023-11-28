package com.example.pong_extreme

// Manager defined as object so that there can be only 1 Manager ( Singleton pattern)
object HighscoreManager {

    var classicHighScores:MutableList<Highscore> = mutableListOf(
        Highscore("Max" , 500),
        Highscore("Dennis" , 500),
        Highscore("Juhee", 500),
        Highscore("Mehdi", 500),
        Highscore("Nnoham", 500)
    )
    var timedHighScores:MutableList<Highscore> = mutableListOf(
        Highscore("Speed Max" , 500),
        Highscore("Speedy Boi" , 500),
        Highscore("Fast Man", 500),
        Highscore("Even Faster ", 500)
    )
    fun getHighScores( gameMode: String): MutableList<Highscore> {
        when (gameMode)
        {
            "classic" -> return classicHighScores
            "timed" -> return  timedHighScores
        }
        return classicHighScores // maybe change or add default return
    }
    fun addHighScores( gameMode: String, newHighscore: Highscore)
    {
        when (gameMode)
        {
        "classic" -> classicHighScores.add(newHighscore)
        "timed" ->  timedHighScores.add(newHighscore)
        }
    }
}