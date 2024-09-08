package vn.xdeuhug.text_to_speech_speech_to_text.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 08 / 09 / 2024
 */
class MainViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val _textLiveData = MutableLiveData<String>()
    val textLiveData: LiveData<String> get() = _textLiveData

    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false

    init {
        textToSpeech = TextToSpeech(application, this)
    }

    fun setText(text: String) {
        _textLiveData.value = text
    }

    fun convertTextToSpeech(text: String) {
        if (isTtsInitialized) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.e("TTS", "TextToSpeech chưa được khởi tạo.")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale("vi", "VN")
            isTtsInitialized = true
            Log.e("TTS", "Khởi tạo TextToSpeech thành công.")
        } else {
            Log.e("TTS", "Khởi tạo TextToSpeech thất bại.")
        }
    }

    override fun onCleared() {
        textToSpeech?.shutdown()
        super.onCleared()
    }
}