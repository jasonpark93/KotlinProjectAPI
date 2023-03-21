package service.core.search

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import reactor.core.publisher.Mono
import service.service.core.search.SearchResult
import service.service.model.brand.SearchAPI
import java.util.*

class SearchResultTest {

    private lateinit var searchResult: SearchResult

    @BeforeEach
    fun setUp() {
        // Create mock APIs
        val api1 = mock(SearchAPI::class.java)
        val api2 = mock(SearchAPI::class.java)

        // Configure mock APIs
        `when`(api1.API("Chicken")).thenReturn(
            Mono.just(
                listOf(
                    "<div>Chicken 1</div>",
                    "<div>Chicken 2</div>",
                    "<div>Chicken 3</div>"
                )
            )
        )
        `when`(api2.API("Chicken")).thenReturn(
            Mono.just(
                listOf(
                    "<div>Chicken 2</div>",
                    "<div>Chicken 3</div>",
                    "<div>Chicken 4</div>"
                )
            )
        )

        // Create cache manager with a single cache
        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(listOf(ConcurrentMapCache("allPrintCache")))

        // Create search result object with mock APIs and cache manager
        searchResult = SearchResult(listOf(api1, api2), cacheManager)
    }

    @Test
    fun testResultString() {
        val result = searchResult.resultString("Chicken").block()
        assertEquals("[Chicken 2, Chicken 3, Chicken 1, Chicken 4]", result)
    }

    @Test
    fun testGetFilteredResults() {
        val api1 = searchResult.apis[0]
        val result = searchResult.getFilteredResults(api1, "Chicken").block()
        assertEquals("[Chicken 1, Chicken 2, Chicken 3]", result.toString())
    }

    @Test
    fun testFilterString() {
        val result = searchResult.filterString("<div>Chicken 1</div>").toString()
        assertEquals("Chicken 1", result)
    }

    @Test
    fun testSumStringList() {
        val list1 = listOf("Chicken 1", "Chicken 2", "Chicken 3")
        val list2 = listOf("Chicken 2", "Chicken 3", "Chicken 4")
        val result = searchResult.sumStringList(list1, list2)
        assertEquals("[Chicken 2, Chicken 3, Chicken 1, Chicken 4]", result.toString())
    }
}
