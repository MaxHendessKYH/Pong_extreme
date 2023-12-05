package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Math.abs
import java.lang.Math.sqrt
import kotlin.math.pow


class GameView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback, Runnable {


    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    lateinit var paddle: Paddle
    lateinit var ball: Ball
    lateinit var brickOne: Brick
    lateinit var brickTwo: Brick
    var brickList: MutableList<Brick> = mutableListOf()
    var bounds = Rect()
    var mHolder: SurfaceHolder? = holder


    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
    }
    fun onBallCollision(ball: Ball, paddle: Paddle) {
        if (ball.posX < paddle.posX && ball.posY < paddle.posY) {
            ball.speedX = abs(ball.speedX) * -1
            ball.speedY = abs(ball.speedY) * -1
        }
        if (ball.posX < paddle.posX && ball.posY > paddle.posY) {
            ball.speedX = abs(ball.speedX) * -1
            ball.speedY = abs(ball.speedY)
        }
        if (ball.posX > paddle.posX && ball.posY < paddle.posY) {
            ball.speedX = abs(ball.speedX)
            ball.speedY = abs(ball.speedY) * -1
        }
        if (ball.posX > paddle.posX && ball.posY > paddle.posY) {
            ball.speedX = abs(ball.speedX)
            ball.speedY = abs(ball.speedY)
        }
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
        val distanceY = ball.posY - closestY

        // Check if the distance is less than or equal to the circle's radius
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)
        val radiusSquared = ball.size * ball.size
//        println("Distance" +distanceSquared)

//        println("Radius" +radiusSquared)
        if (distanceSquared <= radiusSquared) {
//            println("Distance" +distanceSquared)

//            println("Radius" +distanceSquared)
            // Collision detected, handle it accordingly (e.g., call a collision handling function)
            onBallCollision(ball, paddle)
        }
    }
    private fun setup() {
        // set paddle
//        paddle = Paddle(this.context, 400f, 1200f,50f, 40f, 30f) gammla
        paddle = Paddle(this.context, 400f,1250f, 250f,28f,0f)
        var posX: Float = 35f
        var posY: Float = 40f
        val numRows = 8
        val numCols = 6

        // set bricks
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val brick = Brick(this.context, 0f + posX, 0f + posY)
                brickList.add(brick)
                posX += 170f
            }
            // Reset posX for the next row and reset posY to the starting position
            posX = 35f
            posY += 90f
        }

        // set ball
        ball = Ball(this.context, Color.YELLOW, 400f, 1200f, 25f, 20f, -20f)
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
    }
    fun draw() {
        canvas = mHolder!!.lockCanvas()

        canvas.drawColor(Color.BLACK)
        paddle.draw(canvas)

        //draw ball
        ball.draw(canvas)
        for (brick in brickList) {
            brick.draw(canvas)
        }

//        //draw brick
//        brickOne.draw(canvas)
//        brickTwo.draw(canvas)


        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mHolder != null){
            mHolder?.addCallback(this)
        }
        setup()
        start()

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        bounds = Rect(0,0,width,height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()

    }

    override fun run() {
        while (running) {
            update()
            draw()
            ball.checkBounds(bounds)
          shapesIntersect(ball, paddle)
        }
    }
//    fun onBallCollision(ball: Ball, paddle: Paddle) {
//        println("COLLISION!!!!")
//        if (ball.posX < paddle.posX && ball.posY < paddle.posY) {
//            ball.speedX = abs(ball.speedX) * -1
//            ball.speedY = abs(ball.speedY) * -1
//        }
//        if (ball.posX < paddle.posX && ball.posY > paddle.posY) {
//            ball.speedX = abs(ball.speedX) * -1
//            ball.speedY = abs(ball.speedY)
//        }
//        if (ball.posX > paddle.posX && ball.posY < paddle.posY) {
//            ball.speedX = abs(ball.speedX)
//            ball.speedY = abs(ball.speedY) * -1
//        }
//        if (ball.posX > paddle.posX && ball.posY < paddle.posY) {
//            ball.speedX = abs(ball.speedX)
//            ball.speedY = abs(ball.speedY)
//        }
//    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        ball.posX  = paddle.posX
//    ball.posY = paddle.posY
        paddle.posX = event!!.x
        return true
    }
}