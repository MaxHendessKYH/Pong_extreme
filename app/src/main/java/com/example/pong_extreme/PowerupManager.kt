package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import kotlin.random.Random

class PowerupManager(context: Context, type: PowerUpType) {
    var bitmap: Bitmap
    var powerupType = PowerUpType.values().random()
    var activePower = "None"
    var powerupActive: Boolean = false
    var stickyTimer = 30
    var slowmotionActive = false
    val context: Context

    companion object{
        enum class PowerUpType {
            BIGPADDLE,
            SMALLPADDLE,
            STICKY,
            SLOWMOTION,
            MULTIBALLS
        }
    }
    init {
        this.context = context
        bitmap = when (type) {
            PowerUpType.BIGPADDLE ->
                BitmapFactory.decodeResource(context.resources, R.drawable.bigpaddle)

            PowerUpType.SMALLPADDLE ->
                BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)

            PowerUpType.STICKY ->
                BitmapFactory.decodeResource(context.resources, R.drawable.sticky)

            PowerUpType.SLOWMOTION ->
                BitmapFactory.decodeResource(context.resources, R.drawable.slowmotion)

            else ->
                BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)
        }
    }

    fun shouldHavePowerup(): Boolean {
        val randomize = Random.nextInt(1, 101)
        return randomize <= 20
    }

    fun activatePowerup(
        paddle: Paddle,
        context: Context,
        balls: MutableList<Ball>,
        ball: Ball,
        type: PowerUpType
    ) {
        powerupActive = true
        // Determine the type of power-up
        powerupType = type//PowerUpType.values().random()
        when (powerupType) {
            PowerUpType.BIGPADDLE -> {
                activePower = "BIGPADDLE"
                paddle.bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.paddle_big)
            }

            PowerUpType.SMALLPADDLE -> {
                activePower = "SMALLPADDLE"
                paddle.bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.paddle_small)
            }

            PowerUpType.SLOWMOTION -> {
                activePower = "SLOWMOTION"
                activateSlowMotionPowerup(ball)
            }

            PowerUpType.STICKY -> {
                setSticky(paddle)
                activePower = "Sticky"
            }

            PowerUpType.MULTIBALLS -> {
                if (balls.size < 3) {
                    balls.add(
                        Ball(
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
                            Color.BLUE,
                            600f,
                            800f,
                            25f,
                            ball.speedX,
                            ball.speedY,
                            isExtraBall = true
                        )
                    )
                }
            }
        }
    }

    fun resetPowerup(paddle: Paddle, ball: Ball) {
        // Reset power-up effects
        powerupActive = false
        paddle.isSticky = false
        when (activePower) {
            "SLOWMOTION" -> resetSlowMotionPowerup(ball)
            "BIGPADDLE" -> resetPaddleSize(paddle)
            "SMALLPADDLE" -> resetPaddleSize(paddle)
            // Add cases for other power-ups if needed
        }
        activePower = "None"
    }

    private fun resetPaddleSize(paddle: Paddle) {
        // Reset paddle to normal size
        paddle.bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paddle)
    }

    //#region sticky
    fun setSticky(paddle: Paddle) {
        if (!paddle.isSticky)
            paddle.isSticky = true
        else if (paddle.isSticky)
            paddle.isSticky = false
    }

    fun checkIfPaddleIsSticky(paddle: Paddle, ball: Ball) {
        // handle sticky paddle shoot ball
        if (paddle.isSticky && ball.ballIsTouchingPaddle) {
            // start countdown until ball shoots from sticky paddle
            stickyPaddleReleaseCountdown(paddle)
        }
        // make sure sticky mode is on after shooting with sticky powerup.
        if (!paddle.isSticky && !ball.ballIsTouchingPaddle && activePower == "Sticky") {
            paddle.isSticky = true
        }
    }

    fun stickyPaddleReleaseCountdown(paddle: Paddle) {
        stickyTimer--
        if (stickyTimer == 0) {
            setSticky(paddle)
            stickyTimer = 30
        }
    }

    //#endregion
    //#region slowmotion
    fun activateSlowMotionPowerup(ball: Ball) {
        slowmotionActive = true
        var slowMotionStartTime = System.currentTimeMillis()
        ball.alterSpeed(0.20f)
    }

    private fun resetSlowMotionPowerup(ball: Ball) {
        // Reset slow motion power-up effects
        slowmotionActive = false
        ball.alterSpeed(5f)
    }

    fun resetBallSpeed(ball: Ball) {
        slowmotionActive = false
        ball.alterSpeed(5f)
    }
    //#endregion
}