package com.example.faststudy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

@SuppressLint("ResourceAsColor")
class RecordVisualizerView (
    context : Context,
     attrs : AttributeSet? = null
    ) : View(context, attrs){
    init {
        setBackgroundColor(R.color.dark_gray)
    }
    var onRequestCurrentAmplitude: (()-> Int)? = null
    // 빈값을 보내서 Int값을 전달받는 형태이며 기본값은 null임
    // 우리가 기본적으로 메인액티비티에서 fun을 대입해주는거임

    /*TODO CostomDrwing은 캠퍼스에다가 드로우윙을 함으로써 그릴 수 있게 되는거임
            Canvas는 무엇을 그릴지 Paint는 어떻게 그릴지 담당을하는 거임
             onDrawing 호출하기전에 미리 Paint를 생성해둬야 함
             onSizeChanged 크기를 어떻게 할것인지*/

   private val ampitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = context.getColor(R.color.purple_500)
        }
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND //라인의 끝을 어떻게 결정 할것인가 말하는건데 둥그렇게
    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes : List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0


    private val visualizeRepeatAction: Runnable = object : Runnable{
        override fun run() {
            if(!isReplaying){
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0

                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
                // 순서가 시간에 맞춰 드로윙하게 된다고 함
            }else{
                replayingPosition++
            }

            invalidate()// 이것을 해줘야함 안하면 데이터는 추가되는데 view가 추가가 되지않음

            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 캠버스에다가 선을 그어 달라고 말할건데
        // 어떤길이로 어떤 간격으  로 그려 달라고 할거냐?
        // Paint에다가 이런 색과 이런 굵기로 추가해달라 할거임

        canvas ?: return //엘비스 연산자 : canvas가 null일때

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes.let { amplituds ->
            if(isReplaying){
                amplituds.takeLast(replayingPosition)}
            else{amplituds} }
            .forEach{ amlitude ->
            val lineLength = amlitude / MAX_AMPLITUDE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE

            if(offsetX < 0 )return@forEach

            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength /2F,
                ampitudePaint
            )

        }

    }
    fun startVisualizing(isReplaying: Boolean){
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing(){
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization(){
        drawingAmplitudes = emptyList()
        invalidate()
    }
    companion object{
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L

    }
    }
