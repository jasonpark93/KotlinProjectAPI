package service.service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface SearchAPI {

    val clientId: String
    val apiUrlTemplate: String
    val arrayKey: String
    val titleName: String
    val requestHeaders: Map<String, String>

    fun WebClient.Builder.setDefaultHeaders(defaultHeaders: Map<String, String>): WebClient.Builder {
        defaultHeaders.forEach { (key, value) -> this.defaultHeader(key, value) }
        return this
    }

    fun get(apiUrl: String, requestHeaders: Map<String, String>): Mono<String> {
        return WebClient.builder()
            .setDefaultHeaders(requestHeaders)
            .build()
            .get()
            .uri(apiUrl)
            .retrieve()
            .bodyToMono(String::class.java)
            .onErrorResume { Mono.just("Failed to connect to API URL $apiUrl") }
    }

    fun API(title: String): Mono<List<String>> {
        val apiUrl = apiUrlTemplate + title
        return get(apiUrl, requestHeaders)
            .map { parseData(it) }
            .defaultIfEmpty(emptyList())
    }

    fun parseData(responseBody: String): List<String> {
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
