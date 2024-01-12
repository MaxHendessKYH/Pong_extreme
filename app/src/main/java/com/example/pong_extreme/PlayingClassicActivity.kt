package com.example.pong_extreme
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.pong_extreme.databinding.ActivityPlayingClassicBinding

class PlayingClassicActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayingClassicBinding
    lateinit var player: Player
    private val handler = Handler()
    private var isUpdateLoopRunning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayingClassicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = Player("classic")
        val gameView = GameView(this, player)
        val container = binding.frameLayout
        container.addView(gameView)
        binding.btnEndGame.setOnClickListener {
            showGameOverDialog()
            gameView.gameOver()
        }
        startUpdateLoop()
    }

    override fun onDestroy() {
        stopUpdateLoop()
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun startUpdateLoop() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isUpdateLoopRunning) {
                    return
                }
                // update lives text dynamicly
                    binding.tvLives.text = "Lives: " + player.showLives().toString()
                binding.tvScore.text = "Score: " + player.getScore().toString()

                //Game over - end Game
                if (player.showLives() <= 0) {
                    stopUpdateLoop()
                    showGameOverDialog()

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
    private fun showGameOverDialog()
    {
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
       val input = EditText(this)
       builder.setView(input)
        builder.setTitle("Game Over!")
        builder.setMessage("Enter Name:")
        builder.setPositiveButton("Submit Score" ) { dialog, id ->
                  HighscoreManager.addHighScores(Highscore(input.text.toString(), player.getScore(), "classic"), prefs)
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


    private fun restartGame() {
        val intent = Intent(this, PlayingClassicActivity::class.java)
        startActivity(intent)
        finish()
    }
}