package com.example.pong_extreme

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.pong_extreme.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var binding: ActivityMainBinding
    lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // It makes transparent status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // It will hide the title bar
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Creates a mediaplayer that can loop through the music
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        //When any of the buttons are clicked the music stops and mediaplayer is released
        binding.btnClassic.setOnClickListener {
            mediaPlayer.stop()
            val intent = Intent(this, PlayingClassicActivity::class.java)
            startActivity(intent)
        }

        binding.btnTimed.setOnClickListener {
            mediaPlayer.stop()
            val intent = Intent(this, PlayingTimedActivity::class.java)
            startActivity(intent)
        }

        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        setAdapter()
    }

    private fun setAdapter() {
        prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        var classicHighScores = HighscoreManager.getHighScores("classic", prefs)
        val lvClassic = findViewById<ListView>(R.id.lv_highscore_classic)
        val classicAdapter = HighscoreAdapter(this, classicHighScores)
        lvClassic.adapter = classicAdapter

        var timedHighScores = HighscoreManager.getHighScores("timed", prefs)
        val lvTimed = findViewById<ListView>(R.id.lv_highscore_timed)
        val timedAdapter = HighscoreAdapter(this, timedHighScores)
        lvTimed.adapter = timedAdapter
    }

}