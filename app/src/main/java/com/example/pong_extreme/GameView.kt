package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameView (context: Context?): SurfaceView(context), SurfaceHolder.Callback, Runnable {


    var thread: Thread? = null
    var running = false
    lateinit var canvas: Canvas
    lateinit var paddle: Paddle
    var mHolder : SurfaceHolder? = holder



    init {
        if (mHolder != null){
            mHolder?.addCallback(this)
        }

        setup()
    }



    private fun setup(){
        paddle = Paddle(this.context, 400f, 0f)
    }



    fun start(){
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop(){
        running = false
        thread?.join()
    }
    fun update(){
        paddle.update(width.toFloat())
    }


    fun draw(){

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        start()
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        paddle.draw(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stop()

    }

    override fun run() {
        while(running){
            update()
            draw()

        }

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        paddle.posX = event!!.x
        return true
    }
}