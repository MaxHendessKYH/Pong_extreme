package com.example.pong_extreme

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.pong_extreme.databinding.ActivityMainBinding


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
    override fun onResume() {
        super.onResume()
        setAdapter()
    }
    private fun setAdapter()
    {

        var classicHighScores = HighscoreManager.getHighScores("classic")
        val lvClassic = findViewById<ListView>(R.id.lv_highscore_classic)
        val classicAdapter = HighscoreAdapter(this, classicHighScores)
        lvClassic.adapter = classicAdapter

       var timedHighScores = HighscoreManager.getHighScores("timed")
        val lvTimed = findViewById<ListView>(R.id.lv_highscore_timed)
        val timedAdapter = HighscoreAdapter(this, timedHighScores)
      lvTimed.adapter = timedAdapter
    }
}