package service.model.brand

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ResultAPI(
    private val naverAPI: NaverAPI,
    private val kakaoAPI: KakaoAPI
) {
    fun allPrint(title: String): String {
        val a = kakaoAPI.API(title).subList(0, 5).map {
            filterString(it)
        }
        val b = naverAPI.API(title).subList(0, 5).map {
            filterString(it)
        }
        return sumStringList(a, b).toString()
    }

    fun filterString(str: String): String {
        val regex = Regex("<[^>]*>")
        return str.split(" ")[0].replace(regex, "")
    }

    fun sumStringList(list1: List<String>, list2: List<String>): MutableSet<String> {
        val duplicates = mutableSetOf<String>()
        val result = mutableSetOf<String>()

        for (str in list1) {
            if (list2.contains(str) && !duplicates.contains(str)) {
                duplicates.add(str)
                result.add(str)
            }
        }

        for (str in list2) {
            if (duplicates.contains(str) && !result.contains(str)) {
                result.add(str)
            }
        }

        for (str in list1) {
            result.add(str)
        }

        for (str in list2) {
            result.add(str)
        }
        return result
    }

    var immutableMap = mapOf<String, Int>()
    fun add(str: String): Mono<String> {
        immutableMap = immutableMap + (str to (immutableMap.getOrDefault(str, 0) + 1))
        return Mono.just(immutableMap.toString())
    }

    fun print(): String {
        return immutableMap.toString()
    }
}
