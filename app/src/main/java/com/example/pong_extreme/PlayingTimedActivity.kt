package com.example.pong_extreme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pong_extreme.databinding.ActivityPlayingTimedBinding

class PlayingTimedActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayingTimedBinding
    lateinit var countDownTimer: CountDownTimer
    var initialMillis: Long = 1000L
    var remainingMillis: Long = initialMillis
    lateinit var player: Player
    private val handler = Handler()
    private var isUpdateLoopRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayingTimedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        player = Player("timed")
        //Start the counter when activity is started, this time i set the timer on 3 minutes
        Timer(3 * 60 * 1000)
        binding.btnEndGame.setOnClickListener {
            showGameOverDialog()
        }
        val gameView = GameView(this, player)
        val container = binding.frameLayout
        container.addView(gameView)
    }

    private fun showGameOverDialog() {
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        builder.setView(input)
        builder.setTitle("Game Over!")
        builder.setMessage("Enter Name:")
        builder.setPositiveButton("Submit Score") { dialog, id ->
            HighscoreManager.addHighScores(Highscore(input.text.toString(), player.getScore().toString(), "timed"), prefs)
            finish()
        }
        builder.setNeutralButton("Start Menu") { dialog, which ->
            navigateToMainActivity()
            finish()
        }
        builder.setNegativeButton("Try again") { dialog, which ->
            restartGame()
            finish()
        }

        // make button color not white on white
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
            alert.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        alert.show()
    }

    fun addTime(time: Long)
    {
        countDownTimer.cancel()
       remainingMillis += time
        //start new countdown with added time
       Timer(remainingMillis)
    }
    private fun Timer(durationMillis: Long) {
        //Creates a countdowntime
        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick( millisUntilFinished: Long) {
                //line 83 overrides old timer after timer.cancel() and displays new timers values
                remainingMillis =millisUntilFinished
                // Update the textview with whats left of the time
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTime.text = String.format("%02d:%02d", minutes, seconds)
                if (player.getLevelComplete()) {
                    addTime(30000L) // add 30 sec
                    player.setLevelComplete(false)
                }
            }

            override fun onFinish() {
                binding.tvTime.text = "00:00"
                showGameOverDialog()
            }
        }
        countDownTimer.start()
    }
    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        // End timer when activity is destroyed
        countDownTimer.cancel()
        super.onDestroy()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun restartGame() {
        val intent = Intent(this, PlayingTimedActivity::class.java)
        startActivity(intent)
        finish()
    }
}