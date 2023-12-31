package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.view.Display
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import java.lang.Math.abs

class GameView(context: Context?, player: Player) : SurfaceView(context), SurfaceHolder.Callback,
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
    val soundManager = context?.let { SoundManager(it) }

    init {
        this.player = player
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
    }

    private fun setup(currentLevel: Int) {
        // Set paddle
        paddle = Paddle(this.context, 400f, 1250f, 92f, 16f, 0f)

        // Set bricks based on the currentLevel
        when (currentLevel) {
            1 -> levelOneBrickLayout()
            2 -> levelTwoBrickLayout()
            3 -> levelThreeBrickLayout()
            else -> levelOneBrickLayout()
        }

        ball = Ball(this.context, Color.WHITE, 400f, 1200f, 25f, 20f, -20f)

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
        ball.increaseSpeed(speedFactor)
    }


    private fun levelOneBrickLayout() {
        // Set up paddle
        paddle = Paddle(this.context, 400f, 1250f, 250f, 28f, 0f)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of rows and columns
        val numRows = 8
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks
        var color: Int = 1
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            for (col in 0 until numCols) {
                val brick = Brick(this.context, 0f + posX, 0f + posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and reset posY to the starting position
            posX = 10f
            posY += 85f
        }
    }

    private fun levelTwoBrickLayout() {
        // Set up paddle (same as levelOneBrickLayout)
        paddle = Paddle(this.context, 400f, 1250f, 250f, 28f, 0f)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of columns based on screen width
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks in a triangular pattern
        val numRows = numCols
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            for (col in 0 until minOf(row + 1, numCols)) {
                val brick = Brick(this.context, 0f + posX, 0f + posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and reset posY to the starting position
            posX = 10f
            posY += 85f
        }
    }

    private fun levelThreeBrickLayout() {
        // Set up paddle (same as levelOneBrickLayout)
        paddle = Paddle(this.context, 400f, 1250f, 250f, 28f, 0f)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of columns based on screen width
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks in a triangular pattern with a centered starting point for each row
        val numRows = numCols
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            // Calculate the x-center for the current row
            val xCenter = ((numRows - row - 1) / 2f) * (brickWidth + spacing)

            for (col in 0 until minOf(row + 1, numCols)) {
                // Place the brick at a centered x-position and the current y-position
                val brick = Brick(this.context, posX + xCenter, posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and adjust posY for a staggered appearance
            posX = 10f
            posY += (brickWidth + spacing) / 2f
        }
    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop() {
        running = false
        thread?.join()
    }

    fun update() {

        paddle.update(width.toFloat())
        ball.update()

        for (brick in brickList) {
            if (brick.isCollision(ball)) {
                soundManager?.playSoundBrick()
                brickList.remove(brick)
                // Handle any other actions you want to take when a collision occurs
                onBallCollisionBrick(ball, brick)
                if (player.gameMode == "timed") {
                    brokenBrickCount++
                    if (brokenBrickCount == 10 && maxIncreaseCount < 4) {
                        ball.increaseSpeed(1.1f)
                        maxIncreaseCount++
                        brokenBrickCount = 0
                        maxIncreaseCount = 0
                    }
                }

                player.increaseScore(brick.score)
                break // If you want to remove only one brick per frame, otherwise, remove the break statement
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
        start()
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
            ball.checkBounds(bounds)
            // check for collison with bottom of screen
            val hitBottom = ball.checkCollisionBottom(bounds)
            if (hitBottom && player.gameMode == "classic") {
                player.reduceLife()
                // Check for gameover
                if (player.showLives() <= 0) {
                    gameOver()
                    // at this point endgame dialog should show (See classic activity)
                }
            }
            if (hitBottom && player.gameMode == "timed") {
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
            // Put code for hitBottom in timedActivity here
            shapesIntersect(ball, paddle)
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        paddle.posX = event!!.x
        return true
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
    }

    fun onBallCollision(ball: Ball, paddle: Paddle) {
        if (ball.posX < paddle.posX && ball.posY < paddle.posY) {
//            ball.speedX = abs(ball.speedX) * -1
//            ball.speedY = abs(ball.speedY) * -1
            ball.speedX *= -1
            ball.speedY *= -1

        }
        if (ball.posX < paddle.posX && ball.posY > paddle.posY) {
//            ball.speedX = abs(ball.speedX) * -1
//            ball.speedY = abs(ball.speedY)
            ball.speedX *= -1
        }
        if (ball.posX > paddle.posX && ball.posY > paddle.posY) {
//            ball.speedX = abs(ball.speedX)
//            ball.speedY = abs(ball.speedY) * -1
            ball.speedY *= -1

        }
//        if (ball.posX > paddle.posX && ball.posY < paddle.posY ) {
//            ball.speedX = abs(ball.speedX)
//            ball.speedY = abs(ball.speedY)
//        }
        //Plays the sound every time ball and paddle collides
        soundManager?.playSoundPaddle()
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
            distanceY = ball.posY - closestY - 45
        }
        // closestY Pixel2API 33, Pixel 3a behöver - 35

        // Check if the distance is less than or equal to the circle's radius
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)
        val radiusSquared = ball.size * ball.size
//        println("Distance" +distanceSquared)

//        println("Radius" +radiusSquared)
        if (distanceSquared <= radiusSquared) {
            // Collision detected, handle it accordingly (e.g., call a collision handling function)
            onBallCollision(ball, paddle)
        }
    }
}