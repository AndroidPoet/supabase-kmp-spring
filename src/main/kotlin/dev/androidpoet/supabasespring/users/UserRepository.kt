package dev.androidpoet.supabasespring.users

import io.github.androidpoet.supabase.core.result.SupabaseResult

interface UserRepository {
    suspend fun existsByEmail(email: String): SupabaseResult<Boolean>

    suspend fun save(user: NewUser): SupabaseResult<User>

    suspend fun findById(id: String): SupabaseResult<User?>
}
