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

/**
 * The Metro dependency graph — the heart of the app's wiring.
 * It exposes the feature services, binds repository
 * interfaces to their Supabase-backed implementations, and provides the
 * supabase-kmp clients as application singletons. The graph is created once
 * (via [createGraphFactory][dev.zacsweers.metro.createGraphFactory]) and its
 * outputs are handed to Spring as beans (see `MetroConfiguration`).
 */
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
