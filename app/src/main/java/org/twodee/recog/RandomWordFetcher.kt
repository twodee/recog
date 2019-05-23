package org.twodee.recog

import android.os.AsyncTask
import java.lang.ref.WeakReference

// Task
class RandomWordFetcher(
  context: MainActivity,
  private val host: String,
  private val key: String
) : AsyncTask<Unit, Unit, String>() {
  private val context = WeakReference(context)

  override fun doInBackground(vararg p0: Unit): String {
    val endpoint = "https://wordsapiv1.p.mashape.com/words/"
    val parameters = mapOf("random" to "true", "frequencyMin" to "4", "letters" to "5")
    val url = parameterizeUrl(endpoint, parameters)

    val headers = mapOf("X-RapidAPI-Host" to host, "X-RapidAPI-Key" to key)

    val json = getJson(url, headers)
    val word = json.getString("word")

    return word
  }

  override fun onPostExecute(word: String) {
    super.onPostExecute(word)
    context.get()?.let {
      it.word = word
    }
  }
}