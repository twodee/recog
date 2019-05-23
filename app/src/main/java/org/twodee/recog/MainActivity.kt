package org.twodee.recog

import android.Manifest
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import java.lang.ref.WeakReference

class MainActivity : PermittedActivity() {
  private lateinit var wordLabel: TextView
  private lateinit var recognizer: SpeechRecognizer
  private lateinit var recognizeIntent: Intent

  // Task
  var word: String = ""
    set(value) {
      Log.d("FOO", value)
      field = value
      wordLabel.text = value.anagram()
      listen()
    }

  // Task
  private val recognitionListener = object : RecognitionListener {
    override fun onReadyForSpeech(p0: Bundle?) {}
    override fun onRmsChanged(p0: Float) {}
    override fun onBufferReceived(p0: ByteArray?) {}
    override fun onPartialResults(p0: Bundle?) {}
    override fun onEvent(p0: Int, p1: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onEndOfSpeech() {}

    override fun onError(p0: Int) {
      Toast.makeText(this@MainActivity, "Error. Starting over.", Toast.LENGTH_SHORT).show()
    }

    override fun onResults(results: Bundle) {
      Log.d("FOO", results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.toString())
      results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
        checkCandidates(it)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    wordLabel = findViewById(R.id.wordLabel)

    requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 200, {
      initializeSansDialog()
    }, {
      Toast.makeText(this, "Audio recording permission is not granted. Cannot recognize speech.", Toast.LENGTH_LONG).show()
    })

//    generateRandomWord()
  }

  override fun onStop() {
    super.onStop()
    recognizer.stopListening()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.actionbar, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.listenButton -> {
      listen()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  // Task
  private fun generateRandomWord() {
    RandomWordFetcher(this, resources.getString(R.string.words_api_host), resources.getString(R.string.words_api_key)).execute()
  }

  // Task
  private fun initializeSansDialog() {
    recognizeIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
      putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
      putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    recognizer = SpeechRecognizer.createSpeechRecognizer(this)
    recognizer.setRecognitionListener(recognitionListener)

    generateRandomWord()
  }

  // Task
  private fun listen() {
//    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, wordLabel.text)
//    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
//    startActivityForResult(intent, 440)

    recognizer.startListening(recognizeIntent)
  }

  // Task
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      440 -> {
        if (resultCode == RESULT_OK && data != null) {
          val candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
          checkCandidates(candidates)
        }
      }
      else -> super.onActivityResult(requestCode, resultCode, data)
    }
  }

  // Task
  private fun checkCandidates(candidates: ArrayList<String>) {
    if (candidates.any { word.hasSameLetters(it) }) {
      generateRandomWord()
    } else {
      Toast.makeText(this, "Nope. Try again.", Toast.LENGTH_SHORT).show()
      listen()
    }
  }

}

// Task
fun String.anagram(): String {
  var candidate: String
  do {
    candidate = this.toCharArray().toList().shuffled().joinToString("")
  } while (candidate == this)
  return candidate
}

// Task
fun String.hasSameLetters(other: String) =
  this.toCharArray().sort() == other.toCharArray().sort()