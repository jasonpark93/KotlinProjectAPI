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
        return replaceKeyRepository.findByTitle(data.title)
            .flatMap { existingData ->
                existingData.count += 1
                replaceKeyRepository.save(existingData)
            }
            .switchIfEmpty(replaceKeyRepository.save(data))
    }

    override fun getCount(): Mono<String> {
        return replaceKeyRepository.findAll()
            .sort(compareByDescending(ReplaceKey::count))
            .take(10)
            .collectList()
            .map { list ->
                list.joinToString(
                    prefix = "[",
                    postfix = "]",
                    separator = ", "
                ) { "${it.title}=${it.count}" }
            }
    }
}
