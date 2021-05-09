package com.example.audioviewsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.microphonevolumeviewsample.R

/**
 * This class is designed to render sound on the screen.
 * For example, you can use it to show the operation of a microphone while checking it
 * or other audio display tasks.
 */
class AudioView : View {
    /**
     * Default values, that initialize view params if no attributes have been defined.
     */
    private val DEFAULT_CHUNK_COUNT = 9
    private val DEFAULT_BACK_CHUNK_COLOR = Color.WHITE
    private val DEFAULT_FRONT_CHUNK_COLOR = Color.RED
    private val DEFAULT_CHUNK_FORM = ChunkForm.CIRCLE
    private val DEFAULT_SPACE_BETWEEN_CHUNKS = 0f
    private val DEFAULT_CHUNK_WIDTH = 0f
    private val DEFAULT_CHUNK_HEIGHT = 0f
    private val DEFAULT_CIRCLE_CHUNK_RADIUS = 8f

    /**
     * Values, that initialize back chunks params.
     */
    private var backChunkCount = DEFAULT_CHUNK_COUNT
    private var backChunkColor = DEFAULT_BACK_CHUNK_COLOR
        set(value) {
            backChunkPaint.color = value
            field = value
        }
    private val backChunkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backChunkColor }

    /**
     * Values, that initialize front chunks params.
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
     * Common values for back and front chunks.
     * @see MAX_AUDIO_VOLUME is selected manually and seems to be the most optimal.
     */
    private val MAX_AUDIO_VOLUME = 11390f
    private var circleChunkRadius = DEFAULT_CIRCLE_CHUNK_RADIUS
    private var chunkForm = DEFAULT_CHUNK_FORM
    private val volumePerChunk = MAX_AUDIO_VOLUME / backChunkCount
    private var spaceBetweenChunks = DEFAULT_SPACE_BETWEEN_CHUNKS
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

    /**
     * This method updates current audio volume according to
     * @param volume
     */
    fun setVolume(volume: Int) {
        frontChunkCount = (volume / volumePerChunk).toInt()
        invalidate()
    }

    private fun setAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AudioView, 0, 0).apply {
            try {
                backChunkCount = getInt(R.styleable.AudioView_chunk_count, backChunkCount)
                backChunkColor = getColor(R.styleable.AudioView_back_chunk_color, backChunkColor)
                frontChunkColor = getColor(R.styleable.AudioView_front_chunk_color, frontChunkColor)
                spaceBetweenChunks =
                    getDimension(R.styleable.AudioView_space_between_chunks, spaceBetweenChunks)
                chunkHeight = getDimension(R.styleable.AudioView_chunk_height, chunkHeight)
                chunkWidth = getDimension(R.styleable.AudioView_chunk_width, chunkWidth)
                circleChunkRadius =
                    getDimension(R.styleable.AudioView_circle_chunk_radius, circleChunkRadius)
            } finally {
                recycle()
            }
        }
    }

    private fun drawBackCircleChunks(canvas: Canvas) {
        var backChunkCenterX = circleChunkRadius
        val backChunkCenterY = circleChunkRadius
        for (i in 0 until backChunkCount) {
            canvas.drawCircle(backChunkCenterX, backChunkCenterY, circleChunkRadius, backChunkPaint)
            backChunkCenterX += spaceBetweenChunks + circleChunkRadius * 2
        }
    }

    private fun drawFrontCircleChunks(canvas: Canvas) {
        var frontChunkCenterX = circleChunkRadius
        val frontChunkCenterY = circleChunkRadius
        for (i in 0 until frontChunkCount) {
            canvas.drawCircle(
                frontChunkCenterX,
                frontChunkCenterY,
                circleChunkRadius,
                frontChunkPaint
            )
            frontChunkCenterX += spaceBetweenChunks + circleChunkRadius * 2
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