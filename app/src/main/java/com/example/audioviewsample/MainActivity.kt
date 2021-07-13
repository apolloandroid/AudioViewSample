package com.example.audioviewsample

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.microphonevolumeviewsample.R
import com.example.microphonevolumeviewsample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    /**
     * Launcher of record audio request.
     */
    private val recordAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.onRecordAudioPermissionResult(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun setObservers() = with(viewModel) {
        micState.asLiveData().observe(this@MainActivity, {
            setButtonTurnMicrophoneBackground(it)
            setAudioViewVisibility(it)
        })

        requestRecordAudioPermission.asLiveData().observe(this@MainActivity, {
            if (it) recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        })

        micVolume.asLiveData().observe(this@MainActivity, { binding.audioView.setVolume(it) })
    }

    private fun setButtonTurnMicrophoneBackground(micState: MicrophoneState) {
        val backgroundResourceId = when (micState) {
            MicrophoneState.ON -> R.drawable.ic_microphone_on
            MicrophoneState.OFF -> R.drawable.ic_microphone_off
        }
        Glide.with(this).load(backgroundResourceId).into(binding.btnTurnMicrophone)
    }

    private fun setAudioViewVisibility(micState: MicrophoneState) {
        binding.audioView.visibility =
            if (micState == MicrophoneState.ON) View.VISIBLE else View.GONE
    }
}