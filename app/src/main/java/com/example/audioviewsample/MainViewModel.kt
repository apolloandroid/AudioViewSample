package com.example.audioviewsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    /**
     * This value was selected by brute force. You may define it as you like.
     */
    private val DELAY_TIME = 150L

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var isRecordAudioPermissionGranted = false

    private val microphoneChecker = MicrophoneChecker()

    private val _requestRecordAudioPermission = MutableLiveData<Boolean>()
    val requestRecordAudioPermission: LiveData<Boolean> = _requestRecordAudioPermission

    private val _microphoneState = MutableLiveData(MicrophoneState.OFF)
    val microphoneState: LiveData<MicrophoneState> = _microphoneState

    private val _microphoneVolume = MutableLiveData<Int>()
    val microphoneVolume: LiveData<Int> = _microphoneVolume

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        onDestroy()
    }

    fun onDestroy() {
        if (microphoneChecker.isChecking) microphoneChecker.stopChecking()
    }

    fun onTurnMicrophoneButtonClick() = when (isRecordAudioPermissionGranted) {
        true -> {
            when (microphoneState.value) {
                MicrophoneState.OFF -> startMicrophoneChecking()
                else -> stopMicrophoneChecking()
            }
        }
        false -> _requestRecordAudioPermission.value = true
    }

    fun onRecordAudioPermissionResult(isGranted: Boolean) {
        isRecordAudioPermissionGranted = isGranted
        if (isGranted) onTurnMicrophoneButtonClick()
    }

    private fun startMicrophoneChecking() {
        _microphoneState.value = MicrophoneState.ON
        viewModelScope.launch {
            microphoneChecker.startChecking()
            do {
                _microphoneVolume.postValue(microphoneChecker.getMicrophoneVolume())
                delay(DELAY_TIME)
            } while (microphoneChecker.isChecking)
        }
    }

    private fun stopMicrophoneChecking() {
        microphoneChecker.stopChecking()
        _microphoneState.value = MicrophoneState.OFF
    }
}