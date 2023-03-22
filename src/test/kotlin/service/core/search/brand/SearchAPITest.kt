package service.core.search.brand

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import service.service.model.brand.SearchAPI

class SearchAPITest {

    private val testApi = object : SearchAPI {
        override val clientId: String = "test-client-id"
        override val apiUrlTemplate: String = "https://example.com/search?query="
        override val arrayKey: String = "items"
        override val titleName: String = "title"
        override val requestHeaders: Map<String, String> = mapOf()

        override fun API(title: String): Mono<List<String>> {
            return Mono.just(listOf(title))
        }

        override fun parseData(responseBody: String): List<String> {
            return listOf("test-data-1", "test-data-2")
        }
    }

    @Test
    fun testParseData() {
        val testData = """
            {
              "items": [
                {
                  "title": "test-item-1"
                },
                {
                  "title": "test-item-2"
                }
              ]
            }
        """.trimIndent()

        val result = testApi.parseData(testData)
        assertEquals(listOf("test-data-1", "test-data-2"), result)
    }
}
