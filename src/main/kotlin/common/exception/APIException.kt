package service.common.exception

import org.springframework.http.HttpStatus

abstract class APIException(message: String) : RuntimeException(message) {
    abstract val status: HttpStatus
}