package dev.androidpoet.supabasespring.di

import dev.androidpoet.supabasespring.config.AppConfig
import dev.androidpoet.supabasespring.products.ProductService
import dev.androidpoet.supabasespring.users.UserService
import dev.zacsweers.metro.createGraphFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The bridge between Metro and Spring. Spring stays "simple" — it owns the web
 * layer and the bean registry — while Metro owns the actual object graph. We
 * build the [AppGraph] once and republish its outputs as Spring beans so the
 * `@RestController`s can constructor-inject them the usual Spring way.
 */
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
