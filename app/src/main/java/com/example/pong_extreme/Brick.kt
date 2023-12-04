package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint



class Brick(context: Context, var posX: Float, var posY: Float){

    var bitmap: Bitmap

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.brick)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(bitmap, posX, posY, null)
    }
}