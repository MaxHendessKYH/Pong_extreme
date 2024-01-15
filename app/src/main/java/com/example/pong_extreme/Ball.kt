package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class Ball( var color: Int, var posX: Float, var posY: Float, var size: Float, var speedX: Float, var speedY: Float,var isExtraBall: Boolean = false) {
    var paint = Paint()
    var ballIsTouchingPaddle = false
    var isOutOfBounds: Boolean = false
    fun draw(canvas: Canvas?) {
        paint.color = color
        canvas?.drawCircle(posX, posY, size, paint)
    }

    fun update(paddle: Paddle, isTouchingPaddle: Boolean)
    {
        // prevent ball from moving when paddle is sticky and ball is touching paddle
        if (paddle.isSticky && isTouchingPaddle) {

        }
        else
        {
            posX += speedX
            posY += speedY
        }
    }
    fun checkBounds(bounds: Rect){
        // Left Wall
        if(posX-size < bounds.left ){
            speedX *= -1
           posX += 15f
            posX += speedX*1.2f
        }
        // right Wall
        if(posX+size > bounds.right)
        {
            speedX *= -1
            posX -= 15f
            posX += speedX*1.2f
        }
        //Top Wall
        if(posY-size < bounds.top ){
            speedY *= -1
            posY += speedY*1.2f
        }
        //Bottom Wall
        if(posY+size > bounds.bottom) {
            // reset ball position and make it go up
            isOutOfBounds = true
            posX = 400f
            posY = 1200f
            speedY *= -1
        }
    }
    fun checkCollisionBottom(bounds: Rect): Boolean
    {
        if(posY+size > bounds.bottom)
        {
            // reset ball position and make it go up
            posX= 400f
            posY= 1200f
            speedY *=-1
            return true
        }
        return false
    }
    fun getBoundingBox(): RectF {
        return RectF(posX - size, posY - size, posX + size, posY + size)
    }

    fun alterSpeed(factor: Float) {
        speedX *= factor
        speedY *= factor
    }
}