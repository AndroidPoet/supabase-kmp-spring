package dev.androidpoet.supabasespring.di

import dev.androidpoet.supabasespring.config.AppConfig
import dev.androidpoet.supabasespring.products.ProductRepository
import dev.androidpoet.supabasespring.products.ProductService
import dev.androidpoet.supabasespring.products.SupabaseProductRepository
import dev.androidpoet.supabasespring.users.SupabaseUserRepository
import dev.androidpoet.supabasespring.users.UserRepository
import dev.androidpoet.supabasespring.users.UserService
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.androidpoet.supabase.client.Supabase
import io.github.androidpoet.supabase.client.SupabaseClient
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.createDatabaseClient

@DependencyGraph(AppScope::class)
interface AppGraph {
    val userService: UserService
    val productService: ProductService

    @Binds
    val SupabaseUserRepository.bindUserRepository: UserRepository

    @Binds
    val SupabaseProductRepository.bindProductRepository: ProductRepository

    @SingleIn(AppScope::class)
    @Provides
    fun provideSupabaseClient(config: AppConfig): SupabaseClient =
        Supabase.create(
            projectUrl = config.supabase.url,
            apiKey = config.supabase.anonKey,
        ) {
            logging = config.supabase.logging
        }

    @SingleIn(AppScope::class)
    @Provides
    fun provideDatabaseClient(client: SupabaseClient): DatabaseClient =
        createDatabaseClient(client)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides config: AppConfig): AppGraph
    }
}
