package service.service.core.count

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import service.model.meta.ReplaceKey
import service.model.meta.ReplaceKeyRepository

@Service
class H2CountRepository(val replaceKeyRepository: ReplaceKeyRepository) : CountRepository {
    @Transactional
    override fun addCount(data: ReplaceKey): Mono<ReplaceKey> {
        return Mono.defer {
            replaceKeyRepository.findByTitle(data.title)
                .flatMap { existingData ->
                    existingData.count += 1
                    replaceKeyRepository.save(existingData)
                }
                .switchIfEmpty(replaceKeyRepository.save(data))
        }
    }

    override fun getCount(): Mono<String> {
        return Mono.defer {
            replaceKeyRepository.findAll()
                .collectList()
                .flatMap { list -> Mono.just(list) }
        }
            .map { list ->
                list.sortedByDescending(ReplaceKey::count)
                    .take(10)
                    .map { "${it.title}=${it.count}" }
                    .joinToString(prefix = "[", postfix = "]", separator = ", ")
            }
    }
}
