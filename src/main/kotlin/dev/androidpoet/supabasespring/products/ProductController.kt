package dev.androidpoet.supabasespring.products

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val service: ProductService,
) {
    @GetMapping
    fun list(): List<Product> = service.list()

    @GetMapping("/{id}")
    fun byId(@PathVariable id: Long): Product = service.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: ProductRequest): Product = service.create(request.toNewProduct())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ProductRequest): Product =
        service.update(id, request.toPatch())

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)
}
