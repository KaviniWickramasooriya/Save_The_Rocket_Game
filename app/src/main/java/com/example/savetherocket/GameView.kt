package com.example.savetherocket

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 0
    private val otherCars = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time += 10 + speed

        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL

        // Draw player's car (red)
        val redCarDrawable = resources.getDrawable(R.drawable.rocket, null)
        redCarDrawable.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        redCarDrawable.draw(canvas)

        // Draw other cars (yellow)
        myPaint!!.color = Color.YELLOW
        val iterator = otherCars.iterator()
        while (iterator.hasNext()) {
            val car = iterator.next()
            val lane = car["lane"] as Int
            val carX = lane * viewWidth / 3 + viewWidth / 15
            var carY = time - car["startTime"] as Int


            val yellowCarDrawable = resources.getDrawable(R.drawable.astro, null)
            yellowCarDrawable.setBounds(
                carX + 25, carY - carHeight, carX + carWidth - 25, carY
            )
            yellowCarDrawable.draw(canvas)

            if (lane == myCarPosition && carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                gameTask.closeGame(score)
            }

            if (carY > viewHeight + carHeight) {
                iterator.remove()
                score++
                speed = 1 + score / 8
            }
        }

        // Draw score and speed
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }



}
