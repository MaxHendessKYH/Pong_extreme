package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF


class Brick(context: Context, var posX: Float, var posY: Float){

    var bitmap: Bitmap

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.brick)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(bitmap, posX, posY, null)
    }

    fun isCollision(ball: Ball): Boolean {
        val brickRect = RectF(posX, posY, posX + 170f, posY + 90f)
        val ballRect = ball.getBoundingBox()
        return brickRect.intersect(ballRect)
    }

}