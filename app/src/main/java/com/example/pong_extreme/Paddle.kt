package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Paddle (context: Context, var color: Int, var left: Int, var right: Int, var top: Int , var bottom: Int){
    /*
    Left: The x-coordinate (position).
    Top: The y-coordinate  (position).
    Right: (size).
    Bottom:  (size).
    * */
    val paint = Paint()
    private val rectPaint = Paint().apply {
//        color = Color.RED
        style = Paint.Style.FILL
    }

    init {
        paint.color = color
    }

    fun update(){

    }

    fun draw (canvas: Canvas?){
        val rectangle = Rect(left, top, right, bottom)
        canvas?.drawRect(rectangle, paint)
    }








}