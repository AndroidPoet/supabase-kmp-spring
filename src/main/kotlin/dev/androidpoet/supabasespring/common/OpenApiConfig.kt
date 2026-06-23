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
                .title("Supabase + Spring Boot")
                .description("Users & Products CRUD over supabase-kmp.")
                .version("0.0.1"),
        )
}
