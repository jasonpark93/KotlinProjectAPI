package service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

@Service
class KakaoAPI : APIManager {
    override fun API(str: String): MutableList<String> {
        val clientId = "KakaoAK ce730f87d118ffb8542dc38acca06d07" // 애플리케이션 클라이언트 아이디

        var text: String? = null
        try {
            text = URLEncoder.encode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }

        val apiURL =
            "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + text!! // json 결과
        println(apiURL)
        val requestHeaders: HashMap<String, String> = HashMap()
        requestHeaders.put("Authorization", clientId)
        val responseBody = get(apiURL, requestHeaders)

        return parseData(responseBody)
    }

    private fun parseData(responseBody: String): MutableList<String> {
        var title: String
        var jsonObject: JSONObject? = null
        val list = mutableListOf<String>()
        try {
            jsonObject = JSONObject(responseBody)
            println(jsonObject)
            val jsonArray = jsonObject.getJSONArray("documents")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                title = item.getString("place_name")
                list.add(title)
                println("TITLE : $title")
            }
            jsonArray
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }
}
