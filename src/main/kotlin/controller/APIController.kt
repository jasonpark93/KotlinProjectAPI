package service.controller

import org.springframework.cache.CacheManager
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import service.model.brand.KakaoSearchAPI
import service.model.brand.NaverSearchAPI
import service.model.brand.ResultAPI
import service.model.meta.ReplaceKey
import service.model.meta.ReplaceKeyService
import java.util.*

@RestController
class APIController(
    private val replaceKeyService: ReplaceKeyService,
    private val cacheManager: CacheManager
) {
    // 구글이 새로 생겨도 최소한으로 바꿀수 있게 수정
    private val apis = listOf(KakaoSearchAPI(), NaverSearchAPI())
    private val resultAPI = ResultAPI(apis, cacheManager)

    @GetMapping("/search")
    fun search(@RequestBody param: Map<String, String>): Mono<ResponseEntity<String>> {
        val title = param["title"].orEmpty()
        return resultAPI.add(title)
            .flatMap {
                replaceKeyService.insertData(ReplaceKey(title = title))
                resultAPI.allPrintMono(title)
            }
            .map { result ->
                ResponseEntity.status(HttpStatus.OK).headers(HttpHeaders.EMPTY)
                    .body(result)
            }
    }

    @GetMapping("/getCount")
    fun getCount(@RequestBody param: Map<String, String>): Mono<ResponseEntity<String>> {
        return resultAPI.print().map {
            ResponseEntity.status(HttpStatus.OK).headers(HttpHeaders.EMPTY)
                .body(it)
        }
    }
}
