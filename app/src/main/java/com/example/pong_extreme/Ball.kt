package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Ball(context: Context, var color: Int, var posX: Float, var posY: Float, var size: Float, var speedX: Float, var speedY: Float) {

    var paint = Paint()

    fun draw(canvas: Canvas?) {
        paint.color = color
        canvas?.drawCircle(posX, posY, size, paint)
    }

    fun update()
    {
        posX += speedX
        posY += speedY
    }

    fun checkBounds(bounds: Rect){
        if(posX-size < bounds.left || posX+size > bounds.right){
            speedX *= -1
            posX += speedX*1.2f
        }
        if(posY-size < bounds.top || posY+size > bounds.bottom){
            speedY *= -1
            posY += speedY*1.2f
        }
    }
}