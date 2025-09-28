package com.angiedev.chirp.api.exception_handler

import com.angiedev.chirp.domain.exception.InvalidTokenException
import com.angiedev.chirp.domain.exception.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onInvalidToken(
        e: InvalidTokenException
    ) = mapOf(
        "code" to InvalidTokenException.CODE,
        "message" to (e.message)
    )

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun onUserAlreadyExists(
        e: UserAlreadyExistsException
    ) = mapOf(
        "code" to "USER_ALREADY_EXISTS",
        "message" to (e.message)
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onValidationException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, Any?>> {
        val errors = e.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }
        val response = mapOf(
            "code" to "VALIDATION_ERROR",
            "message" to "Validation failed for one or more fields",
            "errors" to errors
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}