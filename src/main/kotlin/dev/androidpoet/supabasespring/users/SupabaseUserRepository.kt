package dev.androidpoet.supabasespring.users

import dev.androidpoet.supabasespring.di.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.client.defaultJson
import io.github.androidpoet.supabase.client.deserialize
import io.github.androidpoet.supabase.core.result.SupabaseResult
import io.github.androidpoet.supabase.core.result.map
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.selectTyped

/**
 * PostgREST-backed [UserRepository] using the supabase-kmp database client.
 * Metro constructs it (`@Inject`) and binds it to [UserRepository] in `AppGraph`.
 */
@Inject
@SingleIn(AppScope::class)
class SupabaseUserRepository(
    private val database: DatabaseClient,
) : UserRepository {
    private val table = "users"

    override suspend fun existsByEmail(email: String): SupabaseResult<Boolean> =
        database
            .selectTyped<User>(table = table) {
                eq("email", email)
                limit(1)
            }.map { it.isNotEmpty() }

    override suspend fun save(user: NewUser): SupabaseResult<User> =
        database
            .insert(
                table = table,
                body = defaultJson.encodeToString(NewUser.serializer(), user),
            ).deserialize<List<User>>()
            .map { it.first() }

    override suspend fun findById(id: String): SupabaseResult<User?> =
        database
            .selectTyped<User>(table = table) {
                eq("id", id)
                limit(1)
            }.map { it.firstOrNull() }
}
