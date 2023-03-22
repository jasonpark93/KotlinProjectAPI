package service.service.core.count

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import service.model.meta.ReplaceKey

@Service
class RedisCountRepository(val reactiveRedisTemplate: ReactiveRedisTemplate<String, Int>) :
    CountRepository {

    override fun addCount(data: ReplaceKey): Mono<ReplaceKey> {
        return reactiveRedisTemplate.opsForValue()
            .increment(data.title)
            .thenReturn(data)
    }

    override fun getCount(): Mono<String> {
        return reactiveRedisTemplate.keys("*")
            .flatMap { key ->
                reactiveRedisTemplate.opsForValue().get(key).map { value -> Pair(key, value) }
            }
            .collectList()
            .map { list ->
                list.sortedByDescending { (_, value) -> value ?: 0 }
                    .take(10)
                    .joinToString(
                        prefix = "[",
                        postfix = "]",
                        separator = ", "
                    ) { "${it.first}=${it.second}" }
            }
    }
}
