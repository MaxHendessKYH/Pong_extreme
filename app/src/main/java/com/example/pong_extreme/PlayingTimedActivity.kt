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
    var countDownTimer: CountDownTimer? = null
//    lateinit var countDownTimer: CountDownTimer
    var initialMillis: Long = 1000L
    var remainingMillis: Long = initialMillis
    lateinit var player: Player
    private val handler = Handler()
    private var isUpdateLoopRunning = true
    var livesBegin = 0
    var duration: Long = 0
    lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingTimedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        player = Player("timed")
        livesBegin = player.showLives();
        //Start the counter when activity is started, this time i set the timer on 3 minutes
        duration = 3 * 60 * 1000;
        timer(duration)
        binding.btnEndGame.setOnClickListener {
            showGameOverDialog()
            gameView.gameOver()
        }
        gameView = GameView(this, player)
        val container = binding.frameLayout
        container.addView(gameView)
        startUpdateLoop()
    }

    private fun showGameOverDialog() {
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        builder.setView(input)
        builder.setTitle("Game Over!")
        builder.setMessage("Enter Name:")
        builder.setPositiveButton("Submit Score") { dialog, id ->
            HighscoreManager.addHighScores(
                Highscore(
                    input.text.toString(),
                    player.getScore().toString(),
                    "timed"
                ), prefs
            )
            finish()
        }
        builder.setNeutralButton("Start Menu") { dialog, which ->
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

    private fun timer(durationMillis: Long) {

        //Creates a countdowntime
        if (countDownTimer != null)
            countDownTimer!!.cancel()

        // Create a new CountDownTimer with the updated duration
        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
               remainingMillis =millisUntilFinished
                // Update the textview with what's left of the time
                duration = millisUntilFinished
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                //Formats the string so it shows in for example 03:00
                binding.tvTime.text = String.format("%02d:%02d", minutes, seconds)
                if (player.getLevelComplete()) {
                    addTime(30000L) // add 30 sec
                    player.setLevelComplete(false)
                }
            }
            override fun onFinish() {
                //Sets timer to 00:00 when gameover
                binding.tvTime.text = "00:00"
                gameView.gameOver()

                stopUpdateLoop()
                showGameOverDialog()
            }
        }
        // Start the new timer
        if (countDownTimer != null)
            countDownTimer?.start()
    }
    fun addTime(time: Long)
    {
        countDownTimer?.cancel()
       remainingMillis += time
        //start new countdown with added time
       timer(remainingMillis)
    }
    private fun startUpdateLoop() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isUpdateLoopRunning) {
                    return
                }
                // update score text dynamicly
                binding.tvScore.text = "Score: " + player.getScore().toString()

                //time loss when the ball falls out of bounds : 10 seconds
                // Check if the ball touches the bottom
                if (player.showLives() != livesBegin) {
                    livesBegin = player.showLives()
                    timer(duration - 10 * 1000)
                }

                // make function run every frame, maybe there is a better solution to update lives text?
                handler.postDelayed(
                    this,
                    16
                )

            }
        })
    }

    private fun stopUpdateLoop() {
        isUpdateLoopRunning = false
    }
    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        // End timer when activity is destroyed
        if (countDownTimer != null)

            countDownTimer!!.cancel()
        super.onDestroy()
    }

    private fun restartGame() {
        val intent = Intent(this, PlayingTimedActivity::class.java)
        startActivity(intent)
        finish()
    }
}