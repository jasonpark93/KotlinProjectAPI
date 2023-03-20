package service.model.meta

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReplaceKeyService(val replaceKeyRepository: ReplaceKeyRepository) {
    //    @Transactional
    fun insertData(data: ReplaceKey): Mono<ReplaceKey> {
        return if (data.title != null) {
            data.count += 1
            replaceKeyRepository.save(data)
        } else {
            replaceKeyRepository.findById(1L)
                .flatMap { existingData ->
                    existingData.count += 1
                    replaceKeyRepository.save(existingData)
                }
        }
    }

    fun getCountForTitle(title: String): Mono<Long> {
        return replaceKeyRepository.findAll()
            .filter { data -> data.title == title }
            .reduce(0L) { count, _ -> count + 1 }
    }

    fun getAll(): Flux<ReplaceKey> {
        return replaceKeyRepository.findAll()
    }
}
