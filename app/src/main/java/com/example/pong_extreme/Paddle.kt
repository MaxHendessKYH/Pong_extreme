package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

class Paddle(
    private val context: Context,
    var posX: Float = 100f,
    var posY: Float = 200f,
    var width: Float = 80f,
    val height: Float = 16f,
    var speedX: Float = 0f,
   var type: PaddleType
) {

    var bitmap: Bitmap
    var isSticky: Boolean = false
    init {
        bitmap = when (type) {
            PaddleType.SMALL_PADDLE
            -> BitmapFactory.decodeResource(context.resources, R.drawable.paddle_small)

            PaddleType.BIG_PADDLE
            -> BitmapFactory.decodeResource(context.resources, R.drawable.paddle_big)

            PaddleType.NORMAL_PADDLE
            -> BitmapFactory.decodeResource(context.resources,R.drawable.paddle)
        }
    }

    enum class PaddleType {
        BIG_PADDLE, SMALL_PADDLE, NORMAL_PADDLE
    }

    fun update(surfaceWidth: Float) {
        if (posX + bitmap.width < surfaceWidth) {
            posX += speedX
        } else {
            posX = surfaceWidth - bitmap.width.toFloat()
        }
    }


    fun draw(canvas: Canvas) {
        val centerX = posX
        val centerY = canvas.height.toFloat() - 80f
        canvas?.drawBitmap(bitmap, posX, centerY, null)
    }
}