package dev.androidpoet.supabasespring.products

import dev.androidpoet.supabasespring.common.ValidationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String,
    val description: String? = null,
    val price: Double,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class NewProduct(
    val name: String,
    val description: String? = null,
    val price: Double,
)

@Serializable
data class ProductPatch(
    val name: String,
    val description: String? = null,
    val price: Double,
)

data class ProductRequest(
    val name: String = "",
    val description: String? = null,
    val price: Double? = null,
) {
    private fun validatedPrice(): Double {
        val errors =
            buildMap {
                if (name.isBlank()) put("name", "Name is required")
                else if (name.length > 200) put("name", "Name must be 200 characters or fewer")
                if (price == null) put("price", "Price is required")
                else if (price < 0) put("price", "Price must be zero or greater")
            }
        if (errors.isNotEmpty()) throw ValidationException(errors)
        return price!!
    }

    fun toNewProduct() = NewProduct(name = name.trim(), description = description, price = validatedPrice())

    fun toPatch() = ProductPatch(name = name.trim(), description = description, price = validatedPrice())
}
