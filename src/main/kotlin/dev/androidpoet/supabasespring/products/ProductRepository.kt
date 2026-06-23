package dev.androidpoet.supabasespring.products

import io.github.androidpoet.supabase.core.result.SupabaseResult

/** Port for product persistence (bound to the Supabase adapter via Metro `@Binds`). */
interface ProductRepository {
    suspend fun findAll(): SupabaseResult<List<Product>>

    suspend fun findById(id: Long): SupabaseResult<Product?>

    suspend fun create(product: NewProduct): SupabaseResult<Product>

    suspend fun update(id: Long, patch: ProductPatch): SupabaseResult<Unit>

    suspend fun delete(id: Long): SupabaseResult<Unit>
}
