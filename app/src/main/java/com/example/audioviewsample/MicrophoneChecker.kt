package com.example.audioviewsample

import android.media.MediaRecorder

/**
 * Class for recording voice from microphone and returning its volume.
 */
class MicrophoneChecker {
    /**
     * Default constants.
     */
    private val AUDIO_SAMPLING_RATE = 8000
    private val AUDIO_RECORDING_BITRATE = 12200
    private val OUTPUT_FILE_PATH = "/dev/null"

    /**
     * @see isChecking shows is MicrophoneChecker working or not.
     */
    private var _isChecking = false
    val isChecking: Boolean
        get() = _isChecking

    private lateinit var mediaRecorder: MediaRecorder

    /**
     * Launches MicrophoneChecker.
     */
    fun startChecking() {
        _isChecking = true
        prepareToCheck()
        mediaRecorder.start()
    }

    /**
     * Stops MicrophoneChecker.
     */
    fun stopChecking() {
        _isChecking = false
        mediaRecorder.release()
    }

    /**
     * Returns current microphone volume.
     */
    fun getMicrophoneVolume(): Int {
        return if (this::mediaRecorder.isInitialized) mediaRecorder.maxAmplitude else 0
    }

    /**
     * Prepares MediaRecorder to work.
     */
    private fun prepareToCheck() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.setAudioSamplingRate(AUDIO_SAMPLING_RATE)
        mediaRecorder.setAudioEncodingBitRate(AUDIO_RECORDING_BITRATE)
        mediaRecorder.setOutputFile(OUTPUT_FILE_PATH)
        mediaRecorder.prepare()
    }
}