package dev.androidpoet.supabasespring.products

import dev.androidpoet.supabasespring.di.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.client.defaultJson
import io.github.androidpoet.supabase.client.deserialize
import io.github.androidpoet.supabase.core.result.SupabaseResult
import io.github.androidpoet.supabase.core.result.map
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.deleteUnit
import io.github.androidpoet.supabase.database.selectTyped
import io.github.androidpoet.supabase.database.updateUnitTyped

/**
 * PostgREST-backed [ProductRepository] using the supabase-kmp database client.
 * Same typed-CRUD pattern as the library's todo-crud sample.
 */
@Inject
@SingleIn(AppScope::class)
class SupabaseProductRepository(
    private val database: DatabaseClient,
) : ProductRepository {
    private val table = "products"

    override suspend fun findAll(): SupabaseResult<List<Product>> =
        database.selectTyped(table = table) {
            order("created_at", ascending = false)
        }

    override suspend fun findById(id: Long): SupabaseResult<Product?> =
        database
            .selectTyped<Product>(table = table) {
                eq("id", id)
                limit(1)
            }.map { it.firstOrNull() }

    override suspend fun create(product: NewProduct): SupabaseResult<Product> =
        database
            .insert(
                table = table,
                body = defaultJson.encodeToString(NewProduct.serializer(), product),
            ).deserialize<List<Product>>()
            .map { it.first() }

    override suspend fun update(id: Long, patch: ProductPatch): SupabaseResult<Unit> =
        database.updateUnitTyped(table = table, value = patch) {
            eq("id", id)
        }

    override suspend fun delete(id: Long): SupabaseResult<Unit> =
        database.deleteUnit(table = table) {
            eq("id", id)
        }
}
