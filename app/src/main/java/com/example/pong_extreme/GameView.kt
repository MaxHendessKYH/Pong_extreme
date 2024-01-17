package com.example.pong_extreme

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(
    context: Context?,
    player: Player,
    private val activity: PlayingTimedActivity? = null
) : SurfaceView(context), SurfaceHolder.Callback,
    Runnable {
    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    lateinit var paddle: Paddle
    lateinit var ball: Ball
    var player: Player
    var brickList: MutableList<Brick> = mutableListOf()
    var bounds = Rect()
    var mHolder: SurfaceHolder? = holder
    var currentLevel = 0
    var powerupType = PowerupManager.Companion.PowerUpType.values().random()
    var powerupManager = PowerupManager(this.context, powerupType)
    lateinit var levelManager: LevelManager
    var collisionManager = CollisionManager(player, this)
    val soundManager = context?.let { SoundManager(it) }
    var slowmotionActive = false
    var slowMotionStartTime: Long = 0
    val slowMotionDuration = 15000L
    var powerupActivationTime: Long = 0
    val powerupDurationMillis: Long = 15000 // 15 seconds in milliseconds
    var gameStartCountDownTimer: CountDownTimer? = null
    var alertDialog: AlertDialog? = null
    private var balls: MutableList<Ball> = mutableListOf()

    init {
        this.player = player
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
    }

    private fun setup(currentLevel: Int) {
        // Set paddle and ball start positions
        paddle = Paddle(this.context, 400f, 1250f, 235f, 28f, 0f, Paddle.PaddleType.NORMAL_PADDLE)
        ball = Ball(Color.WHITE, paddle.posX + paddle.width / 2, paddle.posY, 25f, 20f, -20f, false)
        ball.ballIsTouchingPaddle = true
        //Setup current level
        levelManager = LevelManager(paddle, bounds, brickList, this.context, width, height)
        when (currentLevel) {
            1 -> levelManager.levelOneBrickLayout()
            2 -> levelManager.levelTwoBrickLayout()
            3 -> levelManager.levelThreeBrickLayout()
            else -> levelManager.levelOneBrickLayout()
        }
        //If the player is in classic game mode, increase the speed in all levels
        if (player.gameMode == "classic") {
            increaseBallSpeedForLevel(currentLevel)
        }
    }

    private fun increaseBallSpeedForLevel(currentLevel: Int) {
        val speedFactor = when (currentLevel) {
            1 -> 1.0f // Default
            2 -> 1.2f // Increase by 20%
            3 -> 1.4f // Increase by 20%
            else -> 1.0f // Default
        }
        ball.alterSpeed(speedFactor)
    }

    fun startGame() {
        running = true
        thread = Thread(this)
        thread?.start()
        //Starts timer in timedmode
        activity?.startTimer()
    }

    fun stop() {
        running = false
        thread?.join()
    }

    override fun run() {
        while (running) {
            update()
            draw()
        }
    }

    fun update() {
        // Run update methods
        paddle.update(width.toFloat())
        ball.update(paddle, ball.ballIsTouchingPaddle)
        for (ball in balls) {
            ball.update(paddle, ball.ballIsTouchingPaddle)
        }
        //#region Brick collision
        collisionManager.checkForCollisionBrick(brickList, ball)
        for (ball in balls.toList()) {
            collisionManager.checkForCollisionBrick(brickList, ball)
        }
        //#endregion
        //#region Out of bounds
        collisionManager.checkBoundsExtraBalls(balls, bounds)
        collisionManager.checkBoundsMainBall(ball, bounds)
        //#endregion
        //#region Paddle Collision
        collisionManager.checkForCollisionPaddle(ball, paddle ,context)
        for (ball in balls.toList())
        {
            collisionManager.checkForCollisionPaddle(ball, paddle, context)
        }
        //#endregion
        //#region Level finished
        if (levelManager.levelComplete()) {
            currentLevel++
            player.increaseScore(100)
            if (player.gameMode == "timed") {
                player.setLevelComplete(true) // Enables TimedActivity to add more time
            }
            //load level 1 when player has finished all levels
            if (currentLevel > 3) {
                currentLevel = 1
            }
            setup(currentLevel)
        }
        //#endregion
        //#region Powerup Resets
        if (System.currentTimeMillis() - powerupActivationTime >= powerupDurationMillis) {
            powerupManager.resetPowerup(paddle, ball)
        }
        powerupManager.checkIfPaddleIsSticky(paddle, ball)
        if (slowmotionActive) {
            // if time is up reset ballspeed
            if (System.currentTimeMillis() - slowMotionStartTime >= slowMotionDuration) {
                powerupManager.resetBallSpeed(ball)
            }
        }
        //#endregion

        // Update power-ups
        for (powerUp in powerupList.toList()) {
            powerUp.update()

            // Remove power-ups that go beyond the bottom of the screen
            if (powerUp.posY > height) {
                powerupList.remove(powerUp)
                powerUp.recycle() // Clean up resources when power-up is out of screen
            }

            if (powerUp.isCollision(paddle)) {
                // Handle power-up collision with paddle
                powerupManager.activatePowerup(paddle, this.context, balls, ball , powerUp)
                powerupActivationTime = System.currentTimeMillis()
                powerupList.remove(powerUp)
                powerUp.recycle() // Clean up resources when power-up is collected
            }
        }
    }

    fun draw() {
        val currentHolder = mHolder ?: return
        canvas = currentHolder.lockCanvas() ?: return
        try {
            canvas.drawColor(Color.BLACK)
            paddle.draw(canvas)
            ball.draw(canvas)
            for (brick in brickList) {
                brick.draw(canvas)
            }
            for (ball in balls) {
                ball.draw(canvas)
            }
            for (powerUp in powerupList) {
                powerUp.draw(canvas)
            }
        } finally {
            currentHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        currentLevel = 1
        setup(currentLevel)
        draw()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        bounds = Rect(0, 0, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()
        //Releases the instance of soundpool when game ends
        soundManager?.release()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Handles when the user is touching the screen
        if (event?.action == MotionEvent.ACTION_DOWN) {
            //If the game is not running, delay the start with startCountdown
            if (!running) {
                startCountdown()
                return true
            }
        }
        //If the game is running
        if (running) {
            paddle.posX = event?.x ?: paddle.posX
            if (paddle.isSticky && ball.ballIsTouchingPaddle) {
                // TODO:this ball pos is abit wonky fix if we have time
                ball.posX = event?.x ?: (ball.posX + paddle.width / 2)
            }
        }
        return true
    }

    private fun startCountdown() {
        // Start a countdown timer
        gameStartCountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished + 999) / 1000
                val message = "Starting in $secondsRemaining seconds"
                // If the dialog is not yet created, create it
                if (alertDialog == null) {
                    alertDialog = AlertDialog.Builder(context)
                        .setCancelable(false)
                        .create()
                }
                // Update the message in the time left
                alertDialog?.setMessage(message)
                // Show the dialog
                alertDialog?.show()
            }

            override fun onFinish() {
                // Dismiss the dialog when the countdown is finished
                alertDialog?.dismiss()
                // Cancel the countdown timer to prevent further counting
                gameStartCountDownTimer?.cancel()
                // Starts the game or perform any relevant action
                startGame()
            }
        }
        // Start the countdown timer
        gameStartCountDownTimer?.start()
    }

    fun gameOver() {
        ball.speedX = 0f
        ball.speedY = 0f
        for (ball in balls.toList()) {
            ball.speedY = 0f
            ball.speedX = 0f
        }
        if (player.gameMode == "classic" && player.showLives() == 0) {
            soundManager?.playSoundGameOver()
        }
        if (player.gameMode == "timed" && player.timedFinished) {
            soundManager?.playSoundGameOver()
        }
    }

    // Declare a list to store active power-ups in GameView
    var powerupList: MutableList<PowerUp> = mutableListOf()

    fun ballHitBrick(ball: Ball) {
        soundManager?.playSoundBrick()
        //roll for powerup
        if (powerupManager.shouldHavePowerup() && !powerupManager.powerupActive) {
            val powerUp = PowerUp(
                context,
                ball.posX,
                ball.posY,
                type = PowerupManager.Companion.PowerUpType.values().random()
            )
            powerupList.add(powerUp)
        }
    }

    fun ballHitPaddle() {
        soundManager?.playSoundPaddle()
    }
}