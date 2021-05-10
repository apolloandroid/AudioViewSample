package com.example.audioviewsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.microphonevolumeviewsample.R

/**
 * This class is designed to render sound level on the screen.
 * For example, you can use for showing the volume of a microphone while checking it
 * or for other audio display tasks.
 *
 * !ATTENTION!
 * For now, you cannot specify the height and width of this view as "wrap_content".
 * You need to define a specific value.
 */
class AudioView : View {
    /**
     * Default values, that initialize view params if no attributes have been defined.
     */
    private val DEFAULT_BACK_CHUNK_COLOR = Color.WHITE
    private val DEFAULT_FRONT_CHUNK_COLOR = resources.getColor(R.color.default_front_chunk_color)
    private val DEFAULT_CHUNK_COUNT = 9
    private val DEFAULT_SPACE_BETWEEN_CHUNKS = 0f
    private val DEFAULT_CIRCLE_CHUNK_RADIUS = 8f

    /**
     * Values, that initialize back chunks params.
     * Count of back chunks does not change due to audio level.
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
     * Count of front chunks change due to audio level and calculated according to
     * @see setVolume method
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
    private val volumePerChunk = MAX_AUDIO_VOLUME / backChunkCount
    private var spaceBetweenChunks = DEFAULT_SPACE_BETWEEN_CHUNKS


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

    /**
     * Sets declared attributes to this View.
     */
    private fun setAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AudioView, 0, 0).apply {
            try {
                backChunkCount = getInt(R.styleable.AudioView_chunk_count, backChunkCount)
                backChunkColor = getColor(R.styleable.AudioView_back_chunk_color, backChunkColor)
                frontChunkColor = getColor(R.styleable.AudioView_front_chunk_color, frontChunkColor)
                spaceBetweenChunks =
                    getDimension(R.styleable.AudioView_space_between_chunks, spaceBetweenChunks)
                circleChunkRadius =
                    getDimension(R.styleable.AudioView_circle_chunk_radius, circleChunkRadius)
            } finally {
                recycle()
            }
        }
    }

    /**
     * Draws background chunks.
     */
    private fun drawBackCircleChunks(canvas: Canvas) {
        var chunkCenterX = circleChunkRadius
        val chunkCenterY = circleChunkRadius
        for (i in 0 until backChunkCount) {
            canvas.drawCircle(chunkCenterX, chunkCenterY, circleChunkRadius, backChunkPaint)
            chunkCenterX += spaceBetweenChunks + circleChunkRadius * 2
        }
    }

    /**
     * Draws front chunks.
     */
    private fun drawFrontCircleChunks(canvas: Canvas) {
        var chunkCenterX = circleChunkRadius
        val chunkCenterY = circleChunkRadius
        for (i in 0 until frontChunkCount) {
            canvas.drawCircle(chunkCenterX, chunkCenterY, circleChunkRadius, frontChunkPaint)
            chunkCenterX += spaceBetweenChunks + circleChunkRadius * 2
        }
    }
}