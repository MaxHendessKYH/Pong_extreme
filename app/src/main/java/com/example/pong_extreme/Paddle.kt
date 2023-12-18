package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class Paddle (context: Context, var posX: Float,var posY:Float,val width: Float = 92f, val height: Float = 16f, var speedX: Float) {

    var bitmap:Bitmap

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paddle)
    }


    fun update(surfaceWidth: Float) {
        if (posX + bitmap.width < surfaceWidth) {
            posX += speedX
        } else {
            posX = surfaceWidth - bitmap.width.toFloat()
        }
    }

    fun draw(canvas: Canvas){
        val centerX = posX
        val centerY = canvas.height.toFloat() -80f
        canvas?.drawBitmap(bitmap, posX - 80f, posY , null)
    }

}