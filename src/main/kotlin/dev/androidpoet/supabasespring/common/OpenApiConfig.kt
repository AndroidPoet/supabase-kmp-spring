package dev.androidpoet.supabasespring.common

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun apiInfo(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("Supabase + Spring + Metro DI")
                .description("Users & Products CRUD over supabase-kmp, wired with Metro DI.")
                .version("0.0.1"),
        )
}
