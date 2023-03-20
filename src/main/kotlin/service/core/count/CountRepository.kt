package service.service.core.count

import reactor.core.publisher.Mono
import service.model.meta.ReplaceKey

interface CountRepository {
    fun addCount(data: ReplaceKey): Mono<ReplaceKey>
    fun getCount(): Mono<String>
}