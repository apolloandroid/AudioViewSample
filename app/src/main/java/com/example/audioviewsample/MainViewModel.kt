package com.example.audioviewsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val micChecker: MicrophoneChecker
) : ViewModel() {
    /**
     * This value was selected by brute force. You may define it as you like.
     */
    private val DELAY_TIME = 150L
    private val DEFAULT_MIC_VOLUME = 0

    private var isRecordAudioPermissionGranted = false

    private val _requestRecordAudioPermission = MutableStateFlow(false)
    val requestRecordAudioPermission = _requestRecordAudioPermission.asStateFlow()

    private val _micState = MutableStateFlow(MicrophoneState.OFF)
    val micState = _micState.asStateFlow()

    private val _micVolume = MutableStateFlow(DEFAULT_MIC_VOLUME)
    val micVolume = _micVolume.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        onDestroy()
    }

    /**
     * Called when View (Activity or Fragment) is destroyed. Stops microphone checking when.
     */
    fun onDestroy() {
        if (this.micChecker.isChecking) this.micChecker.stopChecking()
    }

    /**
     * Handles pressing the microphone button.
     * First need to request permission to record audio.
     * If received, starts microphone check.
     */
    fun onTurnMicrophoneButtonClick() = when (isRecordAudioPermissionGranted) {
        true -> {
            when (_micState.value) {
                MicrophoneState.OFF -> startMicrophoneChecking()
                else -> stopMicrophoneChecking()
            }
        }
        false -> _requestRecordAudioPermission.value = true
    }

    /**
     * Handles result of recording audio permission request.
     */
    fun onRecordAudioPermissionResult(isGranted: Boolean) {
        isRecordAudioPermissionGranted = isGranted
        if (isGranted) onTurnMicrophoneButtonClick()
    }

    /**
     * Starts microphone checking sets and _microphoneState to MicrophoneState.ON.
     * In a background running coroutine in a loop while the microphone
     * is in MicrophoneState.ON state, requests the value of the current microphone level and
     * updates _microphoneVolume value.
     * This should be done with a delay, because otherwise the value will be updated too quickly
     * and it will not be noticeable on the screen.
     */
    private fun startMicrophoneChecking() {
        _micState.value = MicrophoneState.ON
        viewModelScope.launch {
            this@MainViewModel.micChecker.startChecking()
            do {
                _micVolume.value = this@MainViewModel.micChecker.getMicrophoneVolume()
                delay(DELAY_TIME)
            } while (_micState.value == MicrophoneState.ON)
        }
    }

    /**
     *  Stops microphone checking and sets [_micState] to [MicrophoneState.OFF].
     */
    private fun stopMicrophoneChecking() {
        this.micChecker.stopChecking()
        _micState.value = MicrophoneState.OFF
    }
}