package org.twodee.recog

import android.Manifest
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.system.Os.listen
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

  // Task

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    wordLabel = findViewById(R.id.wordLabel)

//    requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 200, {
//      initializeSansDialog()
//    }, {
//      Toast.makeText(this, "Audio recording permission is not granted. Cannot recognize speech.", Toast.LENGTH_LONG).show()
//    })

    generateRandomWord()
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

  // Task

  // Task

  // Task

  // Task
}

// Task

// Task
