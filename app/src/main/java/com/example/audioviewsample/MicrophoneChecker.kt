package com.example.audioviewsample

import android.media.MediaRecorder

/**
 * Class for recording voice from microphone and showing its volume
 */
class MicrophoneChecker {
    private var _isChecking = false
    val isChecking: Boolean
        get() = _isChecking

    private val AUDIO_SAMPLING_RATE = 8000
    private val AUDIO_RECORDING_BITRATE = 12200
    private val OUTPUT_FILE_PATH = "/dev/null"

    private lateinit var mediaRecorder: MediaRecorder

    fun startChecking() {
        _isChecking = true
        prepareToCheck()
        mediaRecorder.start()
    }

    fun getMicrophoneVolume() = mediaRecorder.maxAmplitude

    fun stopChecking() {
        _isChecking = false
        mediaRecorder.release()
    }

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