package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback, Runnable {


    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    lateinit var paddle: Paddle
    lateinit var ball: Ball
    lateinit var brickOne: Brick
    lateinit var brickTwo: Brick
    var brickList: MutableList<Brick> = mutableListOf()

    var mHolder: SurfaceHolder? = holder


    init {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
    }


    private fun setup() {
        paddle = Paddle(this.context, 400f, 0f)

        var posX: Float = 35f
        var posY: Float = 40f
        val numRows = 8
        val numCols = 6

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

        ball = Ball(this.context, Color.YELLOW, 100f, 100f, 50f, 10f, 10f)

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
                brickList.remove(brick)
                // Handle any other actions you want to take when a collision occurs
                break // If you want to remove only one brick per frame, otherwise, remove the break statement
            }
        }
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

        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mHolder != null) {
            mHolder?.addCallback(this)
        }
        setup()
        start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running) {
            update()
            draw()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        paddle.posX = event!!.x
        return true
    }
}