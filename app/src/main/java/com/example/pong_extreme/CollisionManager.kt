package com.example.pong_extreme
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager
class CollisionManager( player: Player, gameView: GameView) {
    var maxIncreaseCount: Int = 0
    var brokenBrickCount: Int = 0
    var player : Player
    var gameView : GameView
    init {
        this.player = player
        this.gameView = gameView
    }
    fun checkBoundsMainBall(ball : Ball, bounds: Rect)
    {
        ball.checkBounds(bounds)
        //Handle outofbounds for Main Ball
        if (ball.isOutOfBounds && player.gameMode == "classic" && !ball.isExtraBall) {
            player.reduceLife()
            ball.isOutOfBounds = false
            if (player.showLives() <= 0) {
               gameView.gameOver()
                // at this point endgame dialog should show (See classic activity)
            }
        }
        if (ball.isOutOfBounds && player.gameMode == "timed" && !ball.isExtraBall) {
           player.reduceTime(10)
            ball.isOutOfBounds = false
            // Check for gameover
        }
    }
    fun checkBoundsExtraBalls(balls: MutableList<Ball>, bounds :Rect)
    {
        for (ball in balls.toList()) {
            ball.checkBounds(bounds)
            if (ball.isOutOfBounds && ball.isExtraBall) {
                balls.remove(ball)
            }
        }
    }
    fun checkForCollisionBrick(brickList: MutableList<Brick> , ball : Ball) :Boolean{
        for (brick in brickList) {
            if (brick.isCollision(ball)) {
                brickList.remove(brick)
                gameView.ballHitBrick(ball)
                onCollisionBrick(ball, brick)
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
                return true
            }
        }
        return false
    }
    fun onCollisionBrick(ball: Ball, brick: Brick)  {
        if (ball.posX < brick.posX && ball.posY < brick.posY) {
            ball.speedX = Math.abs(ball.speedX) * -1
            ball.speedY = Math.abs(ball.speedY) * -1
        }
        if (ball.posX < brick.posX && ball.posY > brick.posY) {
            ball.speedX = Math.abs(ball.speedX) * -1
            ball.speedY = Math.abs(ball.speedY)
        }
        if (ball.posX > brick.posX && ball.posY < brick.posY) {
            ball.speedX = Math.abs(ball.speedX)
            ball.speedY = Math.abs(ball.speedY) * -1
        }
        if (ball.posX > brick.posX && ball.posY > brick.posY) {
            ball.speedX = Math.abs(ball.speedX)
            ball.speedY = Math.abs(ball.speedY)
        }
    }
    fun checkForCollisionPaddle(ball: Ball, paddle: Paddle, context : Context) {
        // Find the closest point on the square to the center of the circle
        val closestX =
            Math.max(paddle.posX, Math.min(ball.posX, paddle.posX + paddle.width))
        val closestY =
            Math.max(paddle.posY, Math.min(ball.posY, paddle.posY + paddle.height))
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
            onCollisionPaddle(ball, paddle)
        }
        // if ball is not touching paddle set ballIsTouchingPaddle = false
        if (distanceSquared >= radiusSquared) {
            ball.ballIsTouchingPaddle = false
        }
    }
    fun onCollisionPaddle(ball: Ball, paddle: Paddle) {
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
        gameView.ballHitPaddle()
    }
}