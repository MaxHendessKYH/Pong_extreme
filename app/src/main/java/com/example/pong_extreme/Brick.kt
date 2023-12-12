package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF




class Brick(context: Context, var posX: Float, var posY: Float, val width: Float = 56f, val height: Float = 28f, val type: BrickType) {

    var bitmap: Bitmap

    init {
        bitmap = if (type == BrickType.RED) {
            BitmapFactory.decodeResource(context.resources, R.drawable.brick_red)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.brick_blue)
        }
    }

    enum class BrickType {
        RED, BLUE
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