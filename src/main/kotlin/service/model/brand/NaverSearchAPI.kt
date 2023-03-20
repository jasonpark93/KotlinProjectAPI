package service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

@Service
class NaverSearchAPI : SearchAPI {

    override val clientId: String = "0fFQQLqEPVnEVK5YGpIv" // application client id
    val clientSecret: String = "19MSgxKWjL" // application client secret
    override val apiUrlTemplate: String =
        "https://openapi.naver.com/v1/search/local?display=10&query=" // json result
    override val arrayKey: String = "items"
    override val titleName: String = "title"
    override val requestHeaders = mapOf(
        "X-Naver-Client-Id" to clientId,
        "X-Naver-Client-Secret" to clientSecret
    )



}
