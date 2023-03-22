package service.service.core.count

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import service.model.meta.ReplaceKey
import java.util.concurrent.ConcurrentHashMap

@Service
class InMemoryCountRepository : CountRepository {
    private val concurrentMap = ConcurrentHashMap<String, Int>(100000)

    override fun addCount(data: ReplaceKey): Mono<ReplaceKey> {
        return Mono.fromCallable {
            concurrentMap.compute(data.title) { _, count -> count?.plus(1) ?: 1 }
            ReplaceKey(title = data.title)
        }
    }

    override fun getCount(): Mono<String> {
        return Mono.fromCallable {
            concurrentMap.toList()
                .sortedByDescending { (_, value) -> value }
                .take(10)
                .toMap().toString()
        }
    }
}
