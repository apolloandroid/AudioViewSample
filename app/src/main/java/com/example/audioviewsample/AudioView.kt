package com.example.audioviewsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.microphonevolumeviewsample.R

class AudioView : View {
    /**
     * Default values, that initialize the view if no attributes have been defined.
     */
    private val DEFAULT_CHUNK_COUNT = 9
    private val DEFAULT_BACK_CHUNK_COLOR = Color.WHITE
    private val DEFAULT_FRONT_CHUNK_COLOR = Color.RED
    private val DEFAULT_CHUNK_FORM = ChunkForm.CIRCLE
    private val DEFAULT_CHUNK_SPACE = 0f
    private val DEFAULT_CHUNK_WIDTH = 0f
    private val DEFAULT_CHUNK_HEIGHT = 0f
    private val DEFAULT_CIRCLE_CHUNK_RADIUS = 8f

    /**
     * Values, that initialize back chunks.
     */
    private var backChunkCount = DEFAULT_CHUNK_COUNT
    private var backChunkColor = DEFAULT_BACK_CHUNK_COLOR
        set(value) {
            backChunkPaint.color = value
            field = value
        }
    private val backChunkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backChunkColor }

    /**
     * Values, that initialize front chunks.
     */
    private var frontChunkCount = 0
        set(value) {
            field = if (value >= backChunkCount) backChunkCount else value
        }
    private var frontChunkColor = DEFAULT_FRONT_CHUNK_COLOR
        set(value) {
            frontChunkPaint.color = value
            field = value
        }
    private val frontChunkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = frontChunkColor }

    /**
     * Common values.
     */
    private val MAX_AUDIO_VOLUME = 11390f
    private val circleChunkRadius = DEFAULT_CIRCLE_CHUNK_RADIUS
    private var chunkForm = DEFAULT_CHUNK_FORM
    private val perChunk = MAX_AUDIO_VOLUME / backChunkCount
    private var chunkSpace = DEFAULT_CHUNK_SPACE
    private var chunkHeight = DEFAULT_CHUNK_HEIGHT
    private var chunkWidth = DEFAULT_CHUNK_WIDTH


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttributes(attrs)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackCircleChunks(canvas)
        drawFrontCircleChunks(canvas)
    }

    fun setVolume(volume: Int) {
        frontChunkCount = (volume / perChunk).toInt()
        invalidate()
    }

    private fun setAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AudioView, 0, 0).apply {
            try {
                backChunkCount = getInt(R.styleable.AudioView_chunk_count, backChunkCount)
                backChunkColor = getColor(R.styleable.AudioView_back_chunk_color, backChunkColor)
                frontChunkColor = getColor(R.styleable.AudioView_front_chunk_color, frontChunkColor)
                chunkSpace = getDimension(R.styleable.AudioView_chunk_space, chunkSpace)
                chunkHeight = getDimension(R.styleable.AudioView_chunk_height, chunkHeight)
                chunkWidth = getDimension(R.styleable.AudioView_chunk_width, chunkWidth)
//            chunkForm = ChunkForm.from(this.getString(R.styleable.AudioView_chunk_form).toString())
            } finally {
                recycle()
            }
        }
    }

    private fun drawBackCircleChunks(canvas: Canvas) {
        var backChunkCenterX = height.toFloat() / 2
        val backChunkCenterY = height.toFloat() / 2
        for (i in 0 until backChunkCount) {
            canvas.drawCircle(backChunkCenterX, backChunkCenterY, circleChunkRadius, backChunkPaint)
            backChunkCenterX += chunkSpace + circleChunkRadius * 2
        }
    }

    private fun drawFrontCircleChunks(canvas: Canvas) {
        var frontChunkCenterX = height.toFloat() / 2
        val frontChunkCenterY = height.toFloat() / 2
        for (i in 0 until frontChunkCount) {
            canvas.drawCircle(
                frontChunkCenterX,
                frontChunkCenterY,
                circleChunkRadius,
                frontChunkPaint
            )
            frontChunkCenterX += chunkSpace + circleChunkRadius * 2
        }
    }

    enum class ChunkForm(val chunkForm: String?) {
        CIRCLE("circle"),
        RECTANGLE("rectangle");

        companion object {
            fun from(form: String?): ChunkForm = ChunkForm.values().first { it.chunkForm == form }
        }
    }
}