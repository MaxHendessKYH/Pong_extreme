package com.example.pong_extreme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pong_extreme.databinding.ActivityMainBinding
import android.widget.ListView


class MainActivity : AppCompatActivity() {

 lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClassic.setOnClickListener {

            val intent = Intent(this, PlayingClassicActivity::class.java)
            startActivity(intent)
        }
        binding.btnTimed.setOnClickListener {
            val intent = Intent(this, PlayingTimedActivity::class.java)
            startActivity(intent)
        }

        setAdapter()
    }

    private fun setAdapter()
    {
        var dummies = mutableListOf<Highscore>(
            Highscore("Max" , 500),
            Highscore("Dennis" , 500),
            Highscore("Juhee", 500),
            Highscore("Mehdi", 500),
            Highscore("Nnoham", 500),
            Highscore("Nnoham", 500),
            Highscore("Nnoham", 500),
            Highscore("Nnoham", 500),
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