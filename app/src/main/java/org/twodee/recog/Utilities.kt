package org.twodee.recog

import android.net.Uri
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

fun parameterizeUrl(url: String, parameters: Map<String, String>): URL {
  val builder = Uri.parse(url).buildUpon()
  parameters.forEach { (key, value) ->
    builder.appendQueryParameter(key, value)
  }
  val uri = builder.build()
  return URL(uri.toString())
}

fun getJson(url: URL, headers: Map<String, String>): JSONObject {
  val connection = url.openConnection() as HttpsURLConnection

  headers.forEach { (key, value) ->
    connection.setRequestProperty(key, value)
  }

  try {
    val json = BufferedInputStream(connection.inputStream).readBytes().toString(Charset.defaultCharset())
    return JSONObject(json)
  } finally {
    connection.disconnect()
  }
}

