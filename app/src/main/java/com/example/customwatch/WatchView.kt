package com.example.customwatch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
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
    private val lightGreyColor = context.getColor(R.color.light_grey)
    @SuppressLint("NewApi")
    private val darkBlueColor = context.getColor(R.color.dark_blue)

    //colors
    private var rimColor = lightGreyColor
    private var smallLinesColor = lightGreyColor
    private var bigLinesColor = lightGreyColor
    private var centerPointColor = lightGreyColor
    private var secondsHandColor = lightGreyColor
    private var hoursHandColor = lightGreyColor
    private var minutesHandColor = lightGreyColor
    private var backgroundViewColor = darkBlueColor

    //time
    private var seconds = 0
    private var minutes = 0
    private var hours = 0

    init {
        context.withStyledAttributes(attrs,R.styleable.WatchView) {
            rimColor = getColor(R.styleable.WatchView_rimColor, Color.BLACK)
            smallLinesColor = getColor(R.styleable.WatchView_smallLinesColor, Color.BLACK)
            bigLinesColor = getColor(R.styleable.WatchView_bigLinesColor, Color.BLACK)
            centerPointColor = getColor(R.styleable.WatchView_centerPointColor, Color.BLACK)
            secondsHandColor = getColor(R.styleable.WatchView_secondsHandColor, Color.BLACK)
            hoursHandColor = getColor(R.styleable.WatchView_hoursHandColor, Color.BLACK)
            minutesHandColor = getColor(R.styleable.WatchView_minutesHandColor, Color.BLACK)
            backgroundViewColor = getColor(R.styleable.WatchView_backgroundColor, Color.WHITE)
        }
    }

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
        paint.color = backgroundViewColor
        paint.isAntiAlias = true
        canvas?.drawCircle(0f, 0f, radius, paint)
    }

    @SuppressLint("NewApi")
    private fun drawRim(canvas: Canvas?) {
        paint.strokeWidth = 15.0f
        paint.color = rimColor
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
        paint.color = smallLinesColor
        repeat(60) {
            canvas?.withRotation (it * 6f) {
                drawLine(0f,radius,0f, radius - 35f, paint)
            }
        }
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.strokeWidth = 15f
        paint.color = centerPointColor
        canvas?.drawCircle(0f, 0f,10f, paint)
    }

    private fun drawSecondsHand( canvas: Canvas?, seconds: Int) {
        paint.strokeWidth = 4f
        paint.color = secondsHandColor
        canvas?.withRotation(180f + (6f * seconds)) {
            drawLine(0f,-50f, 0f, radius - 30f, paint)
        }
    }

    private fun drawMinutesHand( canvas: Canvas?) {
        paint.strokeWidth = 10f
        paint.color = minutesHandColor
        canvas?.withRotation (180f + (6f * minutes) + 0.08f * seconds) {
            drawLine(0f, 0f, 0f, radius - radius * 0.35f, paint)
        }
    }

    private fun drawHoursHand( canvas: Canvas?) {
        paint.strokeWidth = 10f
        paint.color = hoursHandColor
        canvas?.withRotation(180f + (30f * hours)+ 0.5f * minutes) {
            drawLine(0f, 0f, 0f,radius - radius * 0.55f, paint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        invalidate()
        canvas?.translate((width / 2).toFloat(), (height / 2).toFloat())
        drawBackground(canvas)
        drawRim(canvas)
        drawBigLines(canvas)
        drawSmallLines(canvas)
        drawCenter(canvas)
        drawSecondsHand(canvas,seconds)
        drawHoursHand(canvas)
        drawMinutesHand(canvas)
        invalidate()
    }

}