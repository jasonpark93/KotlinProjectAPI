package service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

interface SearchAPI {

    val clientId: String
    val apiUrlTemplate: String
    val arrayKey: String
    val titleName: String
    val requestHeaders: Map<String, String>

    fun connect(apiUrl: String): HttpURLConnection {
        try {
            val url = URL(apiUrl)
            return url.openConnection() as HttpURLConnection
        } catch (e: IOException) {
            throw RuntimeException("Failed to connect to API URL: $apiUrl", e)
        }
    }

    fun get(apiUrl: String, requestHeaders: Map<String, String>): String {
        val con = connect(apiUrl)
        try {
            con.requestMethod = "GET"
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }
            val responseCode = con.responseCode
            return if (responseCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream)
            } else {
                readBody(con.errorStream)
            }
        } catch (e: IOException) {
            return "Failed to connect to API URL $apiUrl"
        } finally {
            con.disconnect()
        }
    }

    private fun readBody(body: InputStream): String {
        return body.reader().use { it.readText() }
    }

    fun API(str: String): MutableList<String> {
        var text: String? = null
        try {
            text = URLEncoder.encode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("Failed to encode query", e)
        }
        val responseBody = get(apiUrlTemplate + text!!, requestHeaders)
        return parseData(responseBody)
    }

    fun parseData(responseBody: String): MutableList<String> {
        var title: String
        var jsonObject: JSONObject? = null
        val list = mutableListOf<String>()
        try {
            if (!responseBody.contains(arrayKey)) return list
            jsonObject = JSONObject(responseBody)
            val jsonArray = jsonObject.getJSONArray(arrayKey)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                title = item.getString(titleName)
                list.add(title)
            }
            jsonArray
        } catch (e: JSONException) {
            // Print the error message or log it
            println("Error parsing data: ${e.message}")
            // Return an empty list to avoid stopping the program
            return list
        }
        return list
    }
}
