package dev.androidpoet.supabasespring.config

import io.github.androidpoet.supabase.client.Supabase
import io.github.androidpoet.supabase.client.SupabaseClient
import io.github.androidpoet.supabase.database.DatabaseClient
import io.github.androidpoet.supabase.database.createDatabaseClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SupabaseConfiguration {
    private val config = AppConfig.fromEnvironment()

    @Bean
    fun supabaseClient(): SupabaseClient =
        Supabase.create(
            projectUrl = config.supabase.url,
            apiKey = config.supabase.anonKey,
        ) {
            logging = config.supabase.logging
        }

    @Bean
    fun databaseClient(client: SupabaseClient): DatabaseClient = createDatabaseClient(client)
}
