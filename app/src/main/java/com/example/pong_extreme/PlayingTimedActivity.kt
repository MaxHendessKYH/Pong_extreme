package com.example.pong_extreme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pong_extreme.databinding.ActivityPlayingClassicBinding
import com.example.pong_extreme.databinding.ActivityPlayingTimedBinding
import androidx.core.view.WindowCompat

class PlayingTimedActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayingTimedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingTimedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEndGame.setOnClickListener {
            saveScore()
        }
    }
    private fun saveScore()
    {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        builder.setView(input)
        builder.setTitle("Game Over!")
        builder.setMessage("Enter Name:")
        builder.setPositiveButton("Submit Score" ) { dialog, id ->
            HighscoreManager.addHighScores("timed",Highscore(input.text.toString(), 0))
            finish()
        }
        // make button color not white on white
        val alert : AlertDialog =  builder.create()
        alert.setOnShowListener{
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        alert.show()
        // It makes transparent status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // It will hide the title bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_playing_timed)
    }
}