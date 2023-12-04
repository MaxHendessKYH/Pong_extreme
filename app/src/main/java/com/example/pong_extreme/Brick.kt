package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Brick(
    context: Context,
    var color: Int,
    var left: Int,
    var right: Int,
    var top: Int,
    var bottom: Int
) {

    var paint = Paint()

    fun draw(canvas: Canvas?) {
        paint.color = color
        canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
    }
}