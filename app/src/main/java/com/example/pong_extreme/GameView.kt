package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

class GameView (context: Context?): SurfaceView(context), SurfaceHolder.Callback , Runnable {


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

  paddle = Paddle(this.context, Color.GREEN, 200, 300, 300, 350)

    }
    private fun createRectAtBottom(left: Int, top: Int, right: Int, bottom: Int): Paddle {
        return Paddle(context,Color.GREEN,left, top, right, bottom)
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
        paddle.update()
    }

    fun draw(){
       canvas = mHolder!!.lockCanvas()
       paddle.draw(canvas)
      mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        start()

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
}

/*
positonering canvas ?? funkar kanske ???
val surfaceViewHeight = context.resources.getDimension(R.dimen._520dp)
        val surfaceViewWidth = context.resources.getDimension(R.dimen._400dp)
        val initialX = (surfaceViewWidth - 300) / 2
        val initialY = surfaceViewHeight - 200
        val paint = Paint()
        var rect = Rect(initialX.toInt(), initialY.toInt(), initialX.toInt() +300, initialY.toInt() + 200)
      canvas.drawRect( rect , paint)
 */