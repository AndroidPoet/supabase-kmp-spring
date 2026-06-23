package dev.androidpoet.supabasespring.products

import dev.androidpoet.supabasespring.common.NotFoundException
import dev.androidpoet.supabasespring.common.unwrap
import dev.androidpoet.supabasespring.di.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.runBlocking

@Inject
@SingleIn(AppScope::class)
class ProductService(
    private val products: ProductRepository,
) {
    fun list(): List<Product> = runBlocking { products.findAll().unwrap() }

    fun get(id: Long): Product =
        runBlocking {
            products.findById(id).unwrap() ?: throw NotFoundException("Product not found: $id")
        }

    fun create(product: NewProduct): Product = runBlocking { products.create(product).unwrap() }

    fun update(id: Long, patch: ProductPatch): Product =
        runBlocking {
            products.update(id, patch).unwrap()
            products.findById(id).unwrap() ?: throw NotFoundException("Product not found: $id")
        }

    fun delete(id: Long) = runBlocking { products.delete(id).unwrap() }
}
