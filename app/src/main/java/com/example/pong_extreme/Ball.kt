package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(context: Context, var color: Int, var posX: Float, var posY: Float, var size: Float) {

    var paint = Paint()

    fun draw(canvas: Canvas?) {
        paint.color = color
        canvas?.drawCircle(posX, posY, size, paint)
    }
}