package service.service.core.search

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import service.service.model.brand.SearchAPI

@Service
class SearchResult(
    val apis: List<SearchAPI>,
    private val cacheManager: CacheManager
) {
    private val webClient: WebClient = WebClient.create()

    fun allPrintMono(title: String): Mono<String> {
        val cache = cacheManager.getCache("allPrintCache")
        val cacheKey = title

        val cachedResult = cache?.get(cacheKey, String::class.java)
        if (cachedResult != null) {
            return Mono.just(cachedResult)
        }

        return resultString(title).doOnNext { result ->
            cache?.put(cacheKey, result)
        }
    }

    fun resultString(title: String): Mono<String> {
        val firstMono = getFilteredResults(apis[0], title)
        val secondMono = getFilteredResults(apis[1], title)
        return firstMono
            .zipWith(secondMono)
            .map { sumStringList(it.t1, it.t2) }
            .map { it.toString() }
    }

    fun getFilteredResults(api: SearchAPI, title: String): Mono<List<String>> {
        return api.API(title).map { it ->
            println(it)
            it.map {
                filterString(it)
            }.distinct().take(5)
        }
    }

    fun filterString(str: String): String {
        val regex = Regex("<[^>]*>")
        return str.split(" ")[0].replace(regex, "")
//        return str.replace(regex, "")
    }

    fun sumStringList(list1: List<String>, list2: List<String>): MutableSet<String> {
        val duplicates = list1.intersect(list2)
        val result = mutableSetOf<String>()

        result.addAll(duplicates)
        result.addAll(list1)
        result.addAll(list2)

        return result
    }
}
