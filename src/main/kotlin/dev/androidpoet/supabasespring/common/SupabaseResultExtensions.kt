package dev.androidpoet.supabasespring.common

import io.github.androidpoet.supabase.core.result.SupabaseError
import io.github.androidpoet.supabase.core.result.SupabaseErrorCategory
import io.github.androidpoet.supabase.core.result.SupabaseResult
import io.github.androidpoet.supabase.core.result.category
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Bridges supabase-kmp's [SupabaseResult] monad into Spring MVC. On success the
 * value flows straight through; on failure the [SupabaseError.category] is
 * mapped to the matching HTTP status and thrown as a [ResponseStatusException],
 * which the global handler renders as Problem Details.
 */
fun <T> SupabaseResult<T>.unwrap(): T =
    when (this) {
        is SupabaseResult.Success -> value
        is SupabaseResult.Failure -> throw error.toResponseStatusException()
    }

private fun SupabaseError.toResponseStatusException(): ResponseStatusException {
    val status =
        when (category) {
            SupabaseErrorCategory.Conflict -> HttpStatus.CONFLICT
            SupabaseErrorCategory.NotFound -> HttpStatus.NOT_FOUND
            SupabaseErrorCategory.Unauthorized -> HttpStatus.UNAUTHORIZED
            SupabaseErrorCategory.RateLimited -> HttpStatus.TOO_MANY_REQUESTS
            SupabaseErrorCategory.Validation -> HttpStatus.BAD_REQUEST
            SupabaseErrorCategory.Internal -> HttpStatus.BAD_GATEWAY
            SupabaseErrorCategory.Network -> HttpStatus.SERVICE_UNAVAILABLE
            SupabaseErrorCategory.Unknown -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    return ResponseStatusException(status, message)
}
