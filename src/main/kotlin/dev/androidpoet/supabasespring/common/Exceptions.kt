package dev.androidpoet.supabasespring.common

class ValidationException(
    val errors: Map<String, String>,
) : RuntimeException("Validation failed: $errors")

class NotFoundException(
    message: String,
) : RuntimeException(message)

class ConflictException(
    message: String,
) : RuntimeException(message)
