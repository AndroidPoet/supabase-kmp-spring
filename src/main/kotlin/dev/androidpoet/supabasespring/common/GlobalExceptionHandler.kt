package dev.androidpoet.supabasespring.common

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Translates domain exceptions into RFC 7807 Problem Details responses.
 * `ResponseStatusException` (thrown by [unwrap]) is already handled by Spring's
 * defaults.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed").apply {
            title = "Validation Failed"
            setProperty("errors", ex.errors)
        }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Not found").apply {
            title = "Not Found"
        }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Conflict").apply {
            title = "Conflict"
        }
}
