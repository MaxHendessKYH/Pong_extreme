package com.example.pong_extreme

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import java.lang.Math.abs
import java.util.logging.Level

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
    var maxIncreaseCount: Int = 0
    var brokenBrickCount: Int = 0
    var player: Player
    var brickList: MutableList<Brick> = mutableListOf()
    var bounds = Rect()
    var mHolder: SurfaceHolder? = holder
    var currentLevel = 0
    var powerupType = PowerupManager.PowerUpType.values().random()
    var powerupManager = PowerupManager(this.context, powerupType)
    lateinit var levelManager : LevelManager
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
        // Set paddle
        paddle = Paddle(this.context, 400f, 1250f, 250f, 28f, 0f, Paddle.PaddleType.NORMAL_PADDLE)
            levelManager = LevelManager(paddle, bounds, brickList, this.context, width, height)
        // Set bricks based on the currentLevel
        when (currentLevel) {
            1 -> levelManager.levelOneBrickLayout()
            2 -> levelManager.levelTwoBrickLayout()
            3 -> levelManager.levelThreeBrickLayout()
            else -> levelManager.levelOneBrickLayout()
        }

        ball = Ball(this.context, Color.WHITE, 400f, 1200f, 25f, 20f, -20f, false)

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
    fun update() {
        if(powerupActivationTime > 0)
        println(powerupActivationTime)

        paddle.update(width.toFloat())
        ball.update(paddle, ball.ballIsTouchingPaddle)

        for (ball in balls) {
            ball.update(paddle, ball.ballIsTouchingPaddle)
        }

        for (brick in brickList) {
            if (brick.isCollision(ball)) {
                soundManager?.playSoundBrick()
                brickList.remove(brick)
                // Handle any other actions you want to take when a collision occurs
                onBallCollisionBrick(ball, brick)
                if (player.gameMode == "timed") {
                    brokenBrickCount++
                    if (brokenBrickCount == 10 && maxIncreaseCount < 4) {
                        ball.alterSpeed(1.1f)
                        maxIncreaseCount++
                        brokenBrickCount = 0
                        maxIncreaseCount = 0
                    }
                }
                player.increaseScore(brick.score)
                break // If you want to remove only one brick per frame, otherwise, remove the break statement
            }
        }
        // Reset powerup section
        if (System.currentTimeMillis() - powerupActivationTime >= powerupDurationMillis) {
//            resetPaddleSize()

//            powerupManager.powerupActive = false // comment to test time limit on powerups
            powerupManager.resetPowerup(paddle , ball)
        }
        // handle sticky paddle shoot ball
        if (paddle.isSticky && ball.ballIsTouchingPaddle) {
            // start countdown until ball shoots from sticky paddle
            powerupManager.stickyPaddleReleaseCountdown(paddle)
        }
        // make sure sticky mode is on after shooting with sticky powerup.
        if (!paddle.isSticky && !ball.ballIsTouchingPaddle && powerupManager.activePower == "Sticky") {
            paddle.isSticky = true
        }
        if (slowmotionActive) {
            val elapsedTime = System.currentTimeMillis() - slowMotionStartTime
            if (elapsedTime >= slowMotionDuration) {
                resetBallSpeed() // Återställ bollens hastighet när slow motion-tiden har gått ut
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
    fun levelComplete(): Boolean {
        return brickList.isEmpty()
    }
    override fun run() {
        while (running) {
            update()
            draw()


            // Check for the total number of balls
//            if (balls.size < 3) {
//                // Add an extra ball when needed
//                balls.add(Ball(context, Color.RED, 200f, 800f, 20f, 5f, -5f, isExtraBall = true))
//            }
            // Iterate through all balls
            for (ball in balls.toList()) {
                ball.checkBounds(bounds)

                // check for collision with bottom of screen
                val hitBottom = ball.checkCollisionBottom(bounds)

                if (hitBottom && player.gameMode == "classic" && !ball.isExtraBall) {
                    player.reduceLife()

                    // Check for gameover
                    if (player.showLives() <= 0) {
                        gameOver()
                        // at this point endgame dialog should show (See classic activity)
                    }
                }

                if (hitBottom && player.gameMode == "timed" && !ball.isExtraBall) {
                    // Timer goes down by 10 when life is lost
                    player.reduceLife()
                    // Check for gameover
                }

                if (hitBottom && ball.isExtraBall) {
                    balls.remove(ball)
                }
                shapesIntersect(ball, paddle)

            }

            // Check if any ball collides with bricks
            for (ball in balls) {
                for (brick in brickList.toList()) {
                    if (brick.isCollision(ball)) {
                        soundManager?.playSoundBrick()
                        brickList.remove(brick)
                        onBallCollisionBrick(ball, brick)
                        if (player.gameMode == "timed") {
                            brokenBrickCount++
                            if (brokenBrickCount == 10 && maxIncreaseCount < 4) {
                                ball.alterSpeed(1.1f)
                                maxIncreaseCount++
                                brokenBrickCount = 0
                            }
                        }
                        player.increaseScore(brick.score)
                    }
                }
            }



            ball.checkBounds(bounds)
            // check for collison with bottom of screen
            val hitBottom = ball.checkCollisionBottom(bounds)
            if (hitBottom && player.gameMode == "classic" && !ball.isExtraBall) {
                player.reduceLife()
                // Check for gameover
                if (player.showLives() <= 0) {
                    gameOver()
                    // at this point endgame dialog should show (See classic activity)
                }
            }
            if (hitBottom && player.gameMode == "timed" && !ball.isExtraBall) {
                // Timer goes down by 10 when life is lost
                player.reduceLife()
                // Check for gameover
            }
            // Rewards for finishing a level
            if (levelComplete()) {
                currentLevel++
                player.increaseScore(100)
                if (player.gameMode == "timed") {
                    player.setLevelComplete(true)
                }
                if (currentLevel > 3) {
                    currentLevel = 1
                }
                setup(currentLevel)
            }
            shapesIntersect(ball, paddle)


        }

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
    }
    fun onBallCollisionBrick(ball: Ball, brick: Brick) {

        if (ball.posX < brick.posX && ball.posY < brick.posY) {
            ball.speedX = abs(ball.speedX) * -1
            ball.speedY = abs(ball.speedY) * -1
        }
        if (ball.posX < brick.posX && ball.posY > brick.posY) {
            ball.speedX = abs(ball.speedX) * -1
            ball.speedY = abs(ball.speedY)
        }
        if (ball.posX > brick.posX && ball.posY < brick.posY) {
            ball.speedX = abs(ball.speedX)
            ball.speedY = abs(ball.speedY) * -1
        }
        if (ball.posX > brick.posX && ball.posY > brick.posY) {
            ball.speedX = abs(ball.speedX)
            ball.speedY = abs(ball.speedY)
        }

        if (powerupManager.shouldHavePowerup() && !powerupManager.powerupActive) {
            powerupManager.activatePowerup(paddle, this.context, balls, ball)
            powerupActivationTime = powerupDurationMillis
        }
    }
    fun resetBallSpeed() {
        slowmotionActive = false
        ball.alterSpeed(5f)
    }
    fun onBallCollision(ball: Ball, paddle: Paddle) {
        if (ball.posX < paddle.posX && ball.posY < paddle.posY) {
            ball.speedX *= -1
            ball.speedY *= -1
        }
        if (ball.posX < paddle.posX && ball.posY > paddle.posY) {
            ball.speedX *= -1
        }
        if (ball.posX > paddle.posX && ball.posY < paddle.posY) {
            ball.speedY *= -1
        }
        if (ball.posX > paddle.posX && ball.posY > paddle.posY) {
            ball.speedY *= -1
        }
        //Plays the sound every time ball and paddle collides
        soundManager?.playSoundPaddle()
    }
    private fun resetPaddleSize() {
        // Reset paddle to normal size
        paddle.bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paddle)
    }

    fun shapesIntersect(ball: Ball, paddle: Paddle) {
        // Calculate the center of the circle
//        val circleCenterX = ball.posX
//        val circleCenterY = ball.posY
        // Find the closest point on the square to the center of the circle
        val closestX =
            Math.max(this.paddle.posX, Math.min(ball.posX, this.paddle.posX + this.paddle.width))
        val closestY =
            Math.max(this.paddle.posY, Math.min(ball.posY, this.paddle.posY + this.paddle.height))

        // Calculate the distance between the circle center and the closest point on the square
        val distanceX = ball.posX - closestX
        var distanceY = ball.posY - closestY

        // Get info about device #responsive design
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        //TODO: hitta not deprecated lösning för windowmanagern
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // Set values depending on screen size
        if (displayMetrics.heightPixels == 2154 && displayMetrics.widthPixels == 1080) {
            // Pixel 3a
            distanceY = ball.posY - closestY - 41
        } else if (displayMetrics.heightPixels == 2960 && displayMetrics.widthPixels == 1440) {
            // set values för bills telefon
            //Höj -45 till -52 istället
            distanceY = ball.posY - closestY - 52
        }
        // closestY Pixel2API 33, Pixel 3a behöver - 35

        // Check if the distance is less than or equal to the circle's radius
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)
        val radiusSquared = ball.size * ball.size
        if (distanceSquared <= radiusSquared && !ball.ballIsTouchingPaddle) {
            ball.ballIsTouchingPaddle = true
            // Collision detected, handle it accordingly (e.g., call a collision handling function)
            onBallCollision(ball, paddle)
        }
        // if ball is not touching paddle set ballIsTouchingPaddle = false
        if (distanceSquared >= radiusSquared) {
            ball.ballIsTouchingPaddle = false
        }
    }
}
/*
fun drawMore() {
        val currentHolder = mHolder ?: return
        canvas = currentHolder.lockCanvas() ?: return

        try {

            for (ball in balls) {
                ball.draw(canvas)
            }
        } finally {
            currentHolder.unlockCanvasAndPost(canvas)
        }
    }
fun activateSlowMotionPowerup() {
        slowmotionActive = true
        slowMotionStartTime = System.currentTimeMillis()
        ball.alterSpeed(0.20f)
    }
* private fun activatePowerup() {

        powerupManager.powerupActive = true
        // Determine the type of power-up

        when (powerupType) {
            PowerupManager.PowerUpType.BIGPADDLE -> {
                paddle.bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.paddle_big)
            }

            PowerupManager.PowerUpType.SMALLPADDLE -> {
                paddle.bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.paddle_small)
            }

            PowerupManager.PowerUpType.SLOWMOTION -> {
                powerupManager.activePower = "SLOWMOTION"
                activateSlowMotionPowerup()
            }

            PowerupManager.PowerUpType.STICKY -> {
                powerupManager.setSticky(paddle)
                powerupManager.activePower = "Sticky"
            }
            //        Add two  balls power-up is activated
            PowerupManager.PowerUpType.MULTIBALLS -> {
                if (balls.size < 3) {
                    balls.add(
                        Ball(
                            context,
                            Color.RED,
                            200f,
                            800f,
                            25f,
                            ball.speedX,
                            ball.speedY,
                            isExtraBall = true
                        )
                    )
                    balls.add(
                        Ball(
                            context,
                            Color.BLUE,
                            600f,
                            800f,
                            25f,
                            ball.speedX,
                            ball.speedY,
                            isExtraBall = true
                        )
                    )
                    drawMore()
                }
            }

        }

        powerupActivationTime = System.currentTimeMillis()
    }
* */