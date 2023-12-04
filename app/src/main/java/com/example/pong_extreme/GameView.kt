package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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

//        var posX: Float = 20f
//        var posY: Float = 20f
//
//        for (i in 0..5) {
//
//            var brick = Brick(this.context, 0f + posX, 0f + posY)
//            brickList.add(brick)
//            posX += 160f
//            posY += 0f
//            for (i in 0..5) {
//
//                var brick = Brick(this.context, 0f + posX, 0f + posY)
//                brickList.add(brick)
////                posX = 0f
//                posY += 80f
//            }
//            posY = 20f
//        }
//        for (i in 0..5){
//
//            var brick = Brick(this.context,0f+posX,0f+posY)
//            brickList.add(brick)
//            posX = 0f
//            posY += 80f
//        }
//        brickOne = Brick(this.context, 0f, 0f)
//        brickTwo = Brick(this.context, 160f, 80f)


//
//        // Create a yellow ball at position (100, 100) with a size of 50
        ball = Ball(this.context, Color.YELLOW, 100f, 100f, 50f, 10f, 10f)
//
//        // Create a white brick with specified coordinates (0, 300, 300, 350)
//        brickOne = Brick(this.context, Color.WHITE, 0, 300, 300, 350)
//
//        // Create another white brick with different coordinates (500, 800, 400, 450)
//        brickTwo = Brick(this.context, Color.RED, 500, 800, 400, 450)

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