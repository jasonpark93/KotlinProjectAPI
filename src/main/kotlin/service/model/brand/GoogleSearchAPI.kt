package service.service.model.brand

import org.springframework.stereotype.Service

@Service
class GoogleSearchAPI : SearchAPI {
    override val clientId: String = ""
    override val apiUrlTemplate: String = ""
    override val arrayKey: String = ""
    override val titleName: String = ""
    override val requestHeaders = mapOf("Authorization" to clientId)
}
