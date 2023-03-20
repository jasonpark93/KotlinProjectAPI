package service.service.core.search

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import service.model.brand.SearchAPI

@Service
class SearchResult(
    val apis: List<SearchAPI>,
    private val cacheManager: CacheManager
) {

    fun allPrintMono(title: String): Mono<String> {
        val cache = cacheManager.getCache("allPrintCache")
        val cacheKey = title

        val cachedResult = cache?.get(cacheKey, String::class.java)
        if (cachedResult != null) {
            return Mono.just(cachedResult)
        }

        val result = resultString(title)
        cache?.put(cacheKey, result)

        return Mono.just(result)
    }

    fun resultString(title: String): String {
        val first = getFilteredResults(apis[0], title)
        val second = getFilteredResults(apis[1], title)
        return sumStringList(first, second).toString()
    }

    fun getFilteredResults(api: SearchAPI, title: String): List<String> {
        return api.API(title).map {
            filterString(it)
        }.distinct().take(5)
    }

    fun filterString(str: String): String {
        val regex = Regex("<[^>]*>")
        // 띄어쓰기 이후에는 제거하고 비교했다
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
