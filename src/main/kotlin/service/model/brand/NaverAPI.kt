package service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.*
import java.net.URLEncoder
import java.util.HashMap

@Service
class NaverAPI : APIManager {

    override fun API(str: String): MutableList<String> {
        val clientId = "0fFQQLqEPVnEVK5YGpIv" // 애플리케이션 클라이언트 아이디

        val clientSecret = "19MSgxKWjL" // 애플리케이션 클라이언트 시크릿

        var text: String? = null
        try {
            text = URLEncoder.encode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }

        val apiURL =
            "https://openapi.naver.com/v1/search/local?display=10&start=1&sort=random&query=" + text!! // json 결과

        val requestHeaders: HashMap<String, String> = HashMap()
        requestHeaders.put("X-Naver-Client-Id", clientId)
        requestHeaders.put("X-Naver-Client-Secret", clientSecret)
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
            val jsonArray = jsonObject.getJSONArray("items")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                title = item.getString("title")
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
