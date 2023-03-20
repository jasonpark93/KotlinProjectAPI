package service.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import service.common.exception.NotFoundException
import service.common.exception.ErrorResponse
import java.time.LocalDateTime

@RestController
class ExceptionController {
    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(e: NotFoundException): Mono<ResponseEntity<ErrorResponse>> {
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = e.status.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = e.message
        )
        return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse)
        )
    }
}