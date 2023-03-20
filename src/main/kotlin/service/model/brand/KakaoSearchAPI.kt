package service.model.brand

import com.oracle.javafx.jmx.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

@Service
class KakaoSearchAPI : SearchAPI {
    override val clientId: String = "KakaoAK ce730f87d118ffb8542dc38acca06d07" // application client id
    override val apiUrlTemplate: String =
        "https://dapi.kakao.com/v2/local/search/keyword.json?query=" // json result
    override val arrayKey: String = "documents"
    override val titleName: String = "place_name"
    override val requestHeaders = mapOf(
        "Authorization" to clientId
    )
}
