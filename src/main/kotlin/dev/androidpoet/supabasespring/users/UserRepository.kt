package dev.androidpoet.supabasespring.users

import io.github.androidpoet.supabase.core.result.SupabaseResult

/**
 * Port for user persistence. The Supabase-backed adapter is the only
 * implementation; the service depends on this interface (bound via Metro `@Binds`).
 */
interface UserRepository {
    suspend fun existsByEmail(email: String): SupabaseResult<Boolean>

    suspend fun save(user: NewUser): SupabaseResult<User>

    suspend fun findById(id: String): SupabaseResult<User?>
}
