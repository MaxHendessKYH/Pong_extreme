package com.example.pong_extreme

import android.content.Context
import android.graphics.Rect
import android.view.WindowInsetsAnimation

class LevelManager(paddle: Paddle, bounds: Rect, brickList: MutableList<Brick>, context : Context, width: Int , height: Int) {
     var paddle : Paddle
     var bounds: Rect
     var brickList: MutableList<Brick>
     var context : Context
     var width: Int
     var height: Int
    init {
        this.context = context
        this.brickList = brickList
        this.paddle = paddle
        this.bounds = bounds
        this.width = width
        this.height = height
    }
     fun levelOneBrickLayout() {
        // Set up paddle
        paddle = Paddle(this.context, 400f, 1250f, 235f, 28f, 0f, Paddle.PaddleType.NORMAL_PADDLE)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of rows and columns
        val numRows = 8
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks
        var color: Int = 1
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            for (col in 0 until numCols) {
                val brick = Brick(this.context, 0f + posX, 0f + posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and reset posY to the starting position
            posX = 10f
            posY += 85f
        }
    }
     fun levelTwoBrickLayout() {
        // Set up paddle (same as levelOneBrickLayout)
        paddle = Paddle(this.context, 400f, 1250f, 235f, 28f, 0f, Paddle.PaddleType.NORMAL_PADDLE)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of columns based on screen width
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks in a triangular pattern
        val numRows = numCols
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            for (col in 0 until minOf(row + 1, numCols)) {
                val brick = Brick(this.context, 0f + posX, 0f + posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and reset posY to the starting position
            posX = 10f
            posY += 85f
        }
    }
     fun levelThreeBrickLayout() {
        // Set up paddle (same as levelOneBrickLayout)
        paddle = Paddle(this.context, 400f, 1250f, 235f, 28f, 0f, Paddle.PaddleType.NORMAL_PADDLE)

        // Initial position for the bricks
        var posX: Float = 10f
        var posY: Float = 40f

        // Define brick width, spacing, and bounds
        val brickWidth = 150f
        val spacing = 3f
        bounds = Rect(0, 0, width, height)

        // Calculate the number of columns based on screen width
        val numCols = (bounds.width() / (brickWidth + spacing)).toInt()

        // Set up bricks in a triangular pattern with a centered starting point for each row
        val numRows = numCols
        for (row in 0 until numRows) {
            // Alternate between red and blue bricks for each row
            var brickType = if (row % 2 == 0) Brick.BrickType.RED else Brick.BrickType.BLUE

            // Calculate the x-center for the current row
            val xCenter = ((numRows - row - 1) / 2f) * (brickWidth + spacing)

            for (col in 0 until minOf(row + 1, numCols)) {
                // Place the brick at a centered x-position and the current y-position
                val brick = Brick(this.context, posX + xCenter, posY, 28f, type = brickType)
                brickList.add(brick)
                posX += brickWidth + spacing
                brickType =
                    if (brickType == Brick.BrickType.RED) Brick.BrickType.BLUE else Brick.BrickType.RED
            }

            // Reset posX for the next row and adjust posY for a staggered appearance
            posX = 10f
            posY += (brickWidth + spacing) / 2f
        }
    }
    fun levelComplete(): Boolean {
        return brickList.isEmpty()
    }
}