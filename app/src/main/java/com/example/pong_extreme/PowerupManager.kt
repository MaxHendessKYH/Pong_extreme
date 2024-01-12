package com.example.pong_extreme
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random


    class PowerupManager (context: Context, type: PowerUpType) {

        var bitmap: Bitmap
        enum class PowerUpType {
            BIGPADDLE,
            SMALLPADDLE,
            STICKY,
            SLOWMOTION,
            MULTIBALLS
        }

        init {
            bitmap = when (type) {
                PowerUpType.BIGPADDLE ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.bigpaddle)
                PowerUpType.SMALLPADDLE ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)
                PowerUpType.STICKY ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.sticky)
                PowerUpType.SLOWMOTION ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.slowmotion)
                else ->
                    BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)
            }
        }

//        init {
//            bitmap = if (type == PowerUpType.BIGPADDLE) {
//                BitmapFactory.decodeResource(context.resources, R.drawable.bigpaddle)
//            } else if (type == PowerUpType.SMALLPADDLE){
//                BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)
//            }else if (type == PowerUpType.STICKY){
//                BitmapFactory.decodeResource(context.resources, R.drawable.sticky)
//            }else if (type == PowerUpType.SLOWMOTION){
//                BitmapFactory.decodeResource(context.resources, R.drawable.slowmotion)
//            }else {
//                BitmapFactory.decodeResource(context.resources, R.drawable.smallpaddle)
//            }
//        }

    var activePower = "None"
    var powerupActive: Boolean = false
    var stickyTimer = 30
    fun shouldHavePowerup(): Boolean{
        val randomize = Random.nextInt(1,101)
        return randomize <= 20
    }
    fun setSticky(paddle: Paddle)
    {
        if(!paddle.isSticky)
            paddle.isSticky = true
        else if(paddle.isSticky)
            paddle.isSticky = false
    }
    fun stickyPaddleReleaseCountdown(paddle: Paddle)
    {
            stickyTimer--
            if (stickyTimer == 0) {
                setSticky(paddle)
                stickyTimer = 30
            }
    }

}