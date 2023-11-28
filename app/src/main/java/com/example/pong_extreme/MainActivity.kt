package com.example.pong_extreme

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapter()
    }

    private fun setAdapter()
    {
        var dummies = mutableListOf<Highscore>(
            Highscore("Max" , 500),
            Highscore("Dennis" , 500),
            Highscore("Juhee", 500),
            Highscore("Mehdi", 500),
            Highscore("Nnoham", 500)
        )
        val lvClassic = findViewById<ListView>(R.id.lv_highscore_classic)
        val classicAdapter = HighscoreAdapter(this, dummies)
        lvClassic.adapter = classicAdapter

        val lvTimed = findViewById<ListView>(R.id.lv_highscore_timed)
        val timedAdapter = HighscoreAdapter(this, dummies)
        lvTimed.adapter = timedAdapter
    }
}