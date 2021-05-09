package com.example.audioviewsample

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.microphonevolumeviewsample.R
import com.example.microphonevolumeviewsample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val microphonePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.onRecordAudioPermissionResult(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, R.layout.activity_main)

        view.viewModel = viewModel
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun setObservers() = with(viewModel) {
        microphoneState.observe(this@MainActivity, {
            setButtonTurnMicrophoneBackground(it)
            setAudioViewVisibility(it)
        })

        requestRecordAudioPermission.observe(this@MainActivity, {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        })

        microphoneVolume.observe(this@MainActivity, { view.audioView.setVolume(it) })
    }

    private fun setButtonTurnMicrophoneBackground(microphoneState: MicrophoneState) {
        val backgroundResourceId = when (microphoneState) {
            MicrophoneState.ON -> R.drawable.ic_microphone_on
            MicrophoneState.OFF -> R.drawable.ic_microphone_off
        }
        Glide.with(this).load(backgroundResourceId).into(view.buttonTurnMicrophone)
    }

    private fun setAudioViewVisibility(microphoneState: MicrophoneState) {
        view.audioView.visibility =
            if (microphoneState == MicrophoneState.ON) View.VISIBLE else View.GONE
    }
}