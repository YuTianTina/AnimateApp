package com.xg.animateapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

/**
 *
 * 2018/3/15
 * Description: 角标自定义View
 * @author yutt
 */
class CircleNumView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    private var valueNow : String? = null
    private var valueHistory : String? = null
    /**
     * 圆角举证的Rect
     */
    private val rectF = RectF()
    private var fm = Paint.FontMetrics()
    /**
     * 文字高度
     */
    private var textHeight = 0f
    private var textSize: Float by Delegates.notNull()
    private var textColor : Int by Delegates.notNull()
    private var circleBackgroundColor : Int by Delegates.notNull()
    private var textValue : String? = null
    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleNumView)
        textSize = typedArray.getDimension(R.styleable.CircleNumView_textSize, 15f)
        textColor = typedArray.getColor(R.styleable.CircleNumView_textColor, ContextCompat.getColor(context, R.color.white))
        circleBackgroundColor = typedArray.getColor(R.styleable.CircleNumView_circleBackground, ContextCompat.getColor(context, R.color.colorPrimary))
        textValue = typedArray.getString(R.styleable.CircleNumView_text)

        paint.textSize = textSize
        // 抗锯齿
        paint.isAntiAlias = true
        // 文字x轴居中
        paint.textAlign = Paint.Align.CENTER
        // 文字高度
        fm = paint.fontMetrics
        textHeight = Math.abs(fm.descent) + Math.abs(fm.ascent) + Math.abs(fm.leading)
        setText(textValue)
        typedArray.recycle()
    }

    fun setText(value : CharSequence?){
        valueHistory = valueNow
        this.valueNow = value.toString()

        if(value.isNullOrEmpty()){
            animate().alpha(0f)
                    .setDuration(300)
                    .scaleX(0f)
                    .scaleY(0f)
                    .start()
        }else{
            animate().alpha(1f)
                    .setDuration(300)
                    .scaleX(1f)
                    .scaleY(1f)
                    .start()
            animateRectWidth()
        }
    }

    private fun animateRectWidth(){
        val mAnimator = ValueAnimator.ofFloat(getTextWidth(valueHistory)  , getTextWidth(valueNow))
        mAnimator.addUpdateListener {
            val textWidth = it.animatedValue as Float
            rectF.set((width - textWidth)/2f - textHeight / 2, (height - textHeight) / 2f, (width + textWidth) / 2f + textHeight / 2, (height + textHeight) / 2f)
            invalidate()
            requestLayout()
        }
        mAnimator.duration = 300
        mAnimator.setTarget(this)
        mAnimator.start()
    }

    private fun getTextWidth(value : String?) : Float{
        return if(value.isNullOrEmpty() || value?.length ==1){
            return 0f
        }else{
            paint.measureText(value)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(rectF.width().toInt(), rectF.height().toInt())
        setMeasuredDimension(rectF.width().toInt(), rectF.height().toInt())
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(null == valueNow) return
        paint.color = circleBackgroundColor
        paint.style = Paint.Style.FILL
        // 绘制背景的圆角矩形
        canvas?.drawRoundRect(rectF, textHeight / 2, textHeight / 2, paint)


        // 文字需要在x轴并y轴居中
        val y = height / 2 + (Math.abs(fm.ascent) - fm.descent) / 2
        paint.color = textColor
        canvas?.drawText(valueNow, width / 2f, y, paint)
    }
}