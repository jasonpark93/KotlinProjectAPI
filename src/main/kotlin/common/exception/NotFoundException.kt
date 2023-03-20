package service.common.exception

import org.springframework.http.HttpStatus

class NotFoundException(message: String) : APIException(message) {
    override val status: HttpStatus = HttpStatus.NOT_FOUND
}
