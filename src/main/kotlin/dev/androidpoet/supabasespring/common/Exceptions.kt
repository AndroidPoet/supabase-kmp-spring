package dev.androidpoet.supabasespring.common

/** Thrown by request DTOs when field-level validation fails (→ HTTP 400). */
class ValidationException(
    val errors: Map<String, String>,
) : RuntimeException("Validation failed: $errors")

/** A requested resource does not exist (→ HTTP 404). */
class NotFoundException(
    message: String,
) : RuntimeException(message)

/** A uniqueness constraint would be violated (→ HTTP 409). */
class ConflictException(
    message: String,
) : RuntimeException(message)
