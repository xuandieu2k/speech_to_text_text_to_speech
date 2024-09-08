package vn.xdeuhug.text_to_speech_speech_to_text.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import vn.xdeuhug.text_to_speech_speech_to_text.databinding.ActivityMainBinding
import java.util.*
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startSpeechToText()
        } else {
            Toast.makeText(this, "Cần quyền truy cập microphone để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.textLiveData.observe(this) { text ->
            binding.tvText.text = text
        }

        binding.btnSpeechToText.setOnClickListener {
            checkAudioPermissionAndStartSpeechToText()
        }

        binding.btnTextToSpeech.setOnClickListener {
            val text = binding.tvText.text.toString()
            if (text.isNotBlank()) {
                viewModel.convertTextToSpeech(text)
            } else {
                Toast.makeText(this, "Vui lòng nhập văn bản trước", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val speechRecognizerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                viewModel.setText(it)
            }
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
        speechRecognizerLauncher.launch(intent)
    }

    private fun checkAudioPermissionAndStartSpeechToText() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startSpeechToText()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}