package service.model.meta

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ReplaceKeyRepository : R2dbcRepository<ReplaceKey, Long> {
    fun findByTitle(title: String): Mono<ReplaceKey>
}
