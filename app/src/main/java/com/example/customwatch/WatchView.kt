package com.example.customwatch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.withRotation
import java.util.*
import kotlin.math.min

class WatchView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint().apply {
        isAntiAlias = true
    }
    private var radius = 0.0f
    @SuppressLint("NewApi")
    private val bigLinesColor = context.getColor(R.color.light_grey)
    @SuppressLint("NewApi")
    private val smallLinesColor = context.getColor(R.color.light_grey)

    private var seconds = 0
    private var minutes = 0
    private var hours = 0

    fun setTime() {
        val calendar = Calendar.getInstance().apply { Calendar.AM }
        seconds = calendar.get(Calendar.SECOND)
        minutes = calendar.get(Calendar.MINUTE)
        hours = calendar.get(Calendar.HOUR)
        Log.d("SetTime","Set time , seconds $seconds , $hours")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h) / 2.0 * 0.8).toFloat()
    }

    @SuppressLint("NewApi")
    private fun drawBackground(canvas: Canvas?) {
        paint.color = context.getColor(R.color.dark_blue)
        paint.isAntiAlias = true
        canvas?.drawCircle(0f, 0f, radius, paint)
    }

    @SuppressLint("NewApi")
    private fun drawStroke(canvas: Canvas?) {
        paint.strokeWidth = 15.0f
        paint.color = bigLinesColor
        repeat(360) {
            canvas?.withRotation(it*1F) {
                drawPoint(0f,radius,paint)
            }
        }
    }

    private fun drawBigLines(canvas: Canvas?) {
        paint.strokeWidth = 8.0f
        paint.color = bigLinesColor
        repeat(12) {
            canvas?.withRotation (it * 30f) {
                drawLine(0f,radius,0f, radius - 70f, paint)
            }
        }
    }

    private fun drawSmallLines(canvas: Canvas?) {
        paint.strokeWidth = 4.0f
        paint.color = bigLinesColor
        repeat(60) {
            canvas?.withRotation (it * 6f) {
                drawLine(0f,radius,0f, radius - 35f, paint)
            }
        }
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.strokeWidth = 15f
        paint.color = bigLinesColor
        canvas?.drawCircle(0f, 0f,10f, paint)
    }

    private fun drawSecondsHand( canvas: Canvas?, seconds: Int) {
        paint.strokeWidth = 2f
        paint.color = bigLinesColor
        canvas?.withRotation(180f + (6f * seconds)) {
            drawLine(0f,radius - 30f, 0f, 0f, paint)
        }
    }

    private fun drawMinutesHand( canvas: Canvas?) {
        paint.strokeWidth = 10f
        paint.color = bigLinesColor
        canvas?.withRotation (180f + (6f * minutes) + 0.08f * seconds) {
            drawLine(0f, radius - radius * 0.35f, 0f, 0f, paint)
        }
    }

    private fun drawHoursHand( canvas: Canvas?) {
        paint.strokeWidth = 10f
        paint.color = bigLinesColor
        canvas?.withRotation(180f + (30f * hours)+ 0.5f * minutes) {
            drawLine(0f, 0f, 0f,radius - radius * 0.55f, paint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        invalidate()
        canvas?.translate((width / 2).toFloat(), (height / 2).toFloat())
        drawBackground(canvas)
        drawStroke(canvas)
        drawBigLines(canvas)
        drawSmallLines(canvas)
        drawCenter(canvas)
        drawSecondsHand(canvas,seconds)
        drawHoursHand(canvas)
        drawMinutesHand(canvas)
        invalidate()
    }

}