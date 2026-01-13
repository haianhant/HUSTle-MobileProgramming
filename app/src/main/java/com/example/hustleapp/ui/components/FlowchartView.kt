package com.example.hustleapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.hustleapp.R
import com.example.hustleapp.data.remote.RoadmapStepData

/**
 * Custom View to display roadmap steps as a vertical flowchart
 */
class FlowchartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var steps: List<RoadmapStepData> = emptyList()
    
    // Paints
    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary)
        style = Paint.Style.FILL
    }
    
    private val nodeStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary_dark)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary)
        style = Paint.Style.FILL
    }
    
    private val stepNumberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white)
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    
    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_primary)
        textSize = 48f
        isFakeBoldText = true
    }
    
    private val descriptionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_secondary)
        textSize = 36f
    }
    
    private val durationPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.primary)
        textSize = 32f
    }
    
    private val skillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.text_hint)
        textSize = 28f
    }
    
    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.card_background)
        style = Paint.Style.FILL
        setShadowLayer(8f, 0f, 4f, 0x40000000)
    }
    
    // Dimensions
    private val nodeRadius = 30f
    private val cardPadding = 40f
    private val cardMarginStart = 100f
    private val cardMarginEnd = 40f
    private val cardCornerRadius = 24f
    private val lineSpacing = 20f
    private val stepSpacing = 60f
    
    fun setSteps(newSteps: List<RoadmapStepData>) {
        steps = newSteps
        requestLayout()
        invalidate()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        
        // Calculate height based on number of steps
        var totalHeight = paddingTop + paddingBottom
        
        steps.forEach { step ->
            val cardHeight = calculateCardHeight(step, width)
            totalHeight += cardHeight.toInt() + stepSpacing.toInt()
        }
        
        // Add extra space at bottom
        totalHeight += 40
        
        setMeasuredDimension(width, totalHeight)
    }
    
    private fun calculateCardHeight(step: RoadmapStepData, width: Int): Float {
        val cardWidth = width - cardMarginStart - cardMarginEnd - paddingStart - paddingEnd
        
        // Title height
        var height = cardPadding + titlePaint.textSize + lineSpacing
        
        // Description height (estimate 2 lines)
        val descLines = calculateTextLines(step.description, descriptionPaint, cardWidth - cardPadding * 2)
        height += descLines * (descriptionPaint.textSize + 8f) + lineSpacing
        
        // Duration height
        height += durationPaint.textSize + lineSpacing
        
        // Skills height
        if (step.skills.isNotEmpty()) {
            height += skillPaint.textSize + lineSpacing
        }
        
        height += cardPadding
        
        return height
    }
    
    private fun calculateTextLines(text: String, paint: Paint, maxWidth: Float): Int {
        if (text.isEmpty()) return 1
        val words = text.split(" ")
        var lineCount = 1
        var currentLineWidth = 0f
        
        for (word in words) {
            val wordWidth = paint.measureText("$word ")
            if (currentLineWidth + wordWidth > maxWidth) {
                lineCount++
                currentLineWidth = wordWidth
            } else {
                currentLineWidth += wordWidth
            }
        }
        
        return minOf(lineCount, 3) // Max 3 lines
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (steps.isEmpty()) return
        
        // Enable hardware layer for shadows
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        
        var currentY = paddingTop.toFloat() + cardPadding
        val nodeX = paddingStart + cardMarginStart / 2
        val cardWidth = width - cardMarginStart - cardMarginEnd - paddingStart - paddingEnd
        
        steps.forEachIndexed { index, step ->
            val cardHeight = calculateCardHeight(step, width)
            val cardTop = currentY
            val cardBottom = cardTop + cardHeight
            val cardLeft = paddingStart + cardMarginStart
            val cardRight = width - cardMarginEnd - paddingEnd
            
            // Draw connecting line to next step
            if (index < steps.size - 1) {
                val lineStartY = cardTop + nodeRadius
                val lineEndY = cardBottom + stepSpacing - nodeRadius
                canvas.drawLine(nodeX, lineStartY, nodeX, lineEndY, linePaint)
                
                // Draw arrow
                drawArrow(canvas, nodeX, lineEndY - 20f)
            }
            
            // Draw node circle
            canvas.drawCircle(nodeX, cardTop + nodeRadius, nodeRadius, nodePaint)
            canvas.drawCircle(nodeX, cardTop + nodeRadius, nodeRadius, nodeStrokePaint)
            
            // Draw step number
            val textY = cardTop + nodeRadius + stepNumberPaint.textSize / 3
            canvas.drawText(step.stepNumber.toString(), nodeX, textY, stepNumberPaint)
            
            // Draw card
            val cardRect = RectF(cardLeft, cardTop, cardRight, cardBottom)
            canvas.drawRoundRect(cardRect, cardCornerRadius, cardCornerRadius, cardPaint)
            
            // Draw card content
            var textY2 = cardTop + cardPadding + titlePaint.textSize
            
            // Title
            canvas.drawText(
                step.title.take(30) + if (step.title.length > 30) "..." else "",
                cardLeft + cardPadding,
                textY2,
                titlePaint
            )
            textY2 += lineSpacing + descriptionPaint.textSize
            
            // Description (wrap text)
            val descLines = wrapText(step.description, descriptionPaint, cardWidth - cardPadding * 2)
            descLines.take(2).forEach { line ->
                canvas.drawText(line, cardLeft + cardPadding, textY2, descriptionPaint)
                textY2 += descriptionPaint.textSize + 8f
            }
            textY2 += lineSpacing / 2
            
            // Duration
            canvas.drawText("⏱ ${step.duration}", cardLeft + cardPadding, textY2, durationPaint)
            textY2 += durationPaint.textSize + lineSpacing
            
            // Skills
            if (step.skills.isNotEmpty()) {
                val skillsText = "🎯 " + step.skills.take(3).joinToString(", ")
                canvas.drawText(skillsText, cardLeft + cardPadding, textY2, skillPaint)
            }
            
            currentY = cardBottom + stepSpacing
        }
    }
    
    private fun drawArrow(canvas: Canvas, x: Float, y: Float) {
        val path = Path().apply {
            moveTo(x, y + 20f)
            lineTo(x - 12f, y)
            lineTo(x + 12f, y)
            close()
        }
        canvas.drawPath(path, arrowPaint)
    }
    
    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = StringBuilder()
        
        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (paint.measureText(testLine) <= maxWidth) {
                currentLine = StringBuilder(testLine)
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                }
                currentLine = StringBuilder(word)
            }
        }
        
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }
        
        return lines
    }
}
