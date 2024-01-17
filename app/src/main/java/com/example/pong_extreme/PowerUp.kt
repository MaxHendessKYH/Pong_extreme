package com.example.pong_extreme

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class PowerUp(
    context: Context,
    var posX: Float,
    var posY: Float,
    val width: Float = 56f,
    val height: Float = 28f,
    val type: PowerupManager.Companion.PowerUpType
) {
    var bitmap: Bitmap
    var speedY: Float = 5f // Adjust the speed as needed
    init {
        bitmap = getPowerUpBitmap(context, type)
    }

    fun update() {
        posY += speedY
    }
    private fun getPowerUpBitmap(context: Context, type: PowerupManager.Companion.PowerUpType): Bitmap {
        val resourceId = when (type) {
            PowerupManager.Companion.PowerUpType.BIGPADDLE -> R.drawable.bigpaddle
            PowerupManager.Companion.PowerUpType.SMALLPADDLE -> R.drawable.smallpaddle
            PowerupManager.Companion.PowerUpType.SLOWMOTION -> R.drawable.slowmotion
            PowerupManager.Companion.PowerUpType.STICKY -> R.drawable.sticky
            PowerupManager.Companion.PowerUpType.MULTIBALLS -> R.drawable.multiballs
        }
        return BitmapFactory.decodeResource(context.resources, resourceId)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(bitmap, posX, posY, null)
    }

    fun isCollision(paddle: Paddle): Boolean {
        val powerUpRect = RectF(posX, posY, posX + width, posY + height)
        val paddleRect = paddle.getBoundingBox()
        return powerUpRect.intersect(paddleRect)
    }

    // Add a method to clean up resources when they are no longer needed
    fun recycle() {
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}
