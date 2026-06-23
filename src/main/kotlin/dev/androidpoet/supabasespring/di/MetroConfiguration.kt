package dev.androidpoet.supabasespring.di

import dev.androidpoet.supabasespring.config.AppConfig
import dev.androidpoet.supabasespring.products.ProductService
import dev.androidpoet.supabasespring.users.UserService
import dev.zacsweers.metro.createGraphFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetroConfiguration {
    @Bean
    fun appConfig(): AppConfig = AppConfig.fromEnvironment()

    @Bean
    fun appGraph(config: AppConfig): AppGraph = createGraphFactory<AppGraph.Factory>().create(config)

    @Bean
    fun userService(graph: AppGraph): UserService = graph.userService

    @Bean
    fun productService(graph: AppGraph): ProductService = graph.productService
}
