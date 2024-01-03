package com.example.pong_extreme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class Ball(context: Context, var color: Int, var posX: Float, var posY: Float, var size: Float, var speedX: Float, var speedY: Float) {
    var paint = Paint()
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
        if(posX-size < bounds.left || posX+size > bounds.right){
            speedX *= -1
            posX += speedX*1.2f
        }
        if(posY-size < bounds.top ){
            speedY *= -1
            posY += speedY*1.2f
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

    fun increaseSpeed(factor: Float) {
        speedX *= factor
        speedY *= factor
    }
}