package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

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

    fun getBoundingBox(): RectF {
        return RectF(posX - size, posY - size, posX + size, posY + size)
    }
}