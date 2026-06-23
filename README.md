<p align="center">
  <img src="art/banner.png" alt="supabase-kmp + Spring Boot" width="720">
</p>

<h1 align="center">supabase-kmp-spring</h1>

<p align="center">
  <b>Spring Boot · Metro DI · Supabase</b><br/>
  A clean, production-shaped Spring Boot backend whose object graph is wired with
  <a href="https://github.com/ZacSweers/metro">Metro DI</a> and whose data layer is
  <a href="https://supabase.com">Supabase</a>, accessed through the
  <a href="https://github.com/AndroidPoet/supabase-kmp">supabase-kmp</a> SDK.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Metro%20DI-1.1.1-4C6EF5" alt="Metro">
  <img src="https://img.shields.io/badge/Supabase-supabase--kmp%200.9.1-3FCF8E?logo=supabase&logoColor=white" alt="Supabase">
  <img src="https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/License-MIT-orange" alt="License">
</p>

---

## Why this exists

Most "Spring + Supabase" samples reach for the Postgres JDBC driver and an ORM.
This one talks to Supabase the way a **client app** does — over PostgREST through
the [supabase-kmp](https://github.com/AndroidPoet/supabase-kmp) SDK, with Row
Level Security doing the gatekeeping — and wires the whole application with
**compile-time dependency injection (Metro)** instead of Spring's component scan.

The structure is intentionally production-shaped: a `config / di / common /
<feature>` layering, a Metro `@DependencyGraph`, a clean Controller → Service →
Repository-interface → adapter flow, Problem Details errors, request validation,
and auto-generated OpenAPI — with **supabase-kmp** as the data layer instead of a
Postgres driver and ORM.

## The three tools

| Tool | What it does here | Where |
|---|---|---|
| 🧩 **Metro DI** | Builds the application object graph at compile time — services, repositories and the Supabase clients — then hands the result to Spring as beans | [`di/AppGraph.kt`](src/main/kotlin/dev/androidpoet/supabasespring/di/AppGraph.kt), [`di/MetroConfiguration.kt`](src/main/kotlin/dev/androidpoet/supabasespring/di/MetroConfiguration.kt) |
| 📜 **Auto OpenAPI + Swagger UI** | springdoc scans the controllers and serves a live spec + UI — no hand-written schema | [`common/OpenApiConfig.kt`](src/main/kotlin/dev/androidpoet/supabasespring/common/OpenApiConfig.kt) |
| 🟢 **supabase-kmp SDK** | Every read/write: `Supabase.create` → `createDatabaseClient` → `selectTyped` / `insert().deserialize` / `updateUnitTyped` / `deleteUnit` | [`*/Supabase*Repository.kt`](src/main/kotlin/dev/androidpoet/supabasespring/products/SupabaseProductRepository.kt) |

## How Spring and Metro coexist

Spring stays deliberately **simple** — it owns the HTTP layer (`@RestController`)
and the bean registry. **Metro owns the wiring.** The graph is created once and
its outputs are republished as Spring beans, so controllers inject services the
ordinary Spring way:

```kotlin
@DependencyGraph(AppScope::class)
interface AppGraph {
    val userService: UserService
    val productService: ProductService

    @Binds val SupabaseUserRepository.bindUserRepository: UserRepository
    @Binds val SupabaseProductRepository.bindProductRepository: ProductRepository

    @SingleIn(AppScope::class) @Provides
    fun provideSupabaseClient(config: AppConfig): SupabaseClient =
        Supabase.create(config.supabase.url, config.supabase.anonKey) { logging = config.supabase.logging }

    @SingleIn(AppScope::class) @Provides
    fun provideDatabaseClient(client: SupabaseClient): DatabaseClient = createDatabaseClient(client)

    @DependencyGraph.Factory
    fun interface Factory { fun create(@Provides config: AppConfig): AppGraph }
}
```

```kotlin
@Configuration
class MetroConfiguration {
    @Bean fun appConfig() = AppConfig.fromEnvironment()
    @Bean fun appGraph(config: AppConfig) = createGraphFactory<AppGraph.Factory>().create(config)
    @Bean fun userService(graph: AppGraph) = graph.userService
    @Bean fun productService(graph: AppGraph) = graph.productService
}
```

supabase-kmp is **coroutine-first**; services bridge its `suspend` /
`SupabaseResult` API to Spring's blocking model with `runBlocking` + `unwrap()`,
which maps each `SupabaseError.category` to the right HTTP status
([`SupabaseResultExtensions.kt`](src/main/kotlin/dev/androidpoet/supabasespring/common/SupabaseResultExtensions.kt)).

## Project layout

```
src/main/kotlin/dev/androidpoet/supabasespring/
├── DemoApplication.kt
├── config/   AppConfig.kt              # env-driven config (Supabase url/key)
├── di/       AppScope · AppGraph · MetroConfiguration
├── common/   Exceptions · GlobalExceptionHandler · HealthController
│             OpenApiConfig · SupabaseResultExtensions
├── users/    Models · Repository · SupabaseUserRepository · Service · Controller
└── products/ Models · Repository · SupabaseProductRepository · Service · Controller
```

## Getting started

> **Java 21 required.** Metro's Gradle plugin needs the *Gradle JVM* to be 21+.
> `gradle.properties` pins it to a local `openjdk@21`; change that path for your
> machine, or run with `JAVA_HOME` pointing at a JDK 21.

**1. Create the schema** — run [`supabase/migration.sql`](supabase/migration.sql)
in the Supabase SQL editor (creates `users` + `products` with demo anon RLS).

**2. Set credentials** (use the **anon** key, never service-role):

```bash
export SUPABASE_URL="https://YOUR-PROJECT.supabase.co"
export SUPABASE_ANON_KEY="YOUR-ANON-KEY"
```

**3. Run:**

```bash
./gradlew bootRun
```

| | URL |
|---|---|
| Swagger UI | <http://localhost:8080/swagger-ui.html> |
| OpenAPI JSON | <http://localhost:8080/v3/api-docs> |
| Liveness / Readiness | `GET /livez` · `GET /readyz` |

## API

| Method | Path | Body | Result |
|---|---|---|---|
| `POST` | `/api/users` | `{ "email", "displayName" }` | `201` user |
| `GET` | `/api/users/{id}` | — | user |
| `GET` | `/api/products` | — | products |
| `GET` | `/api/products/{id}` | — | product |
| `POST` | `/api/products` | `{ "name", "description?", "price" }` | `201` product |
| `PUT` | `/api/products/{id}` | `{ "name", "description?", "price" }` | product |
| `DELETE` | `/api/products/{id}` | — | `204` |

```bash
curl -s localhost:8080/api/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Coffee","description":"Dark roast","price":12.5}'
```

Errors come back as RFC 7807 Problem Details — validation failures (`400`),
not-found (`404`), conflicts (`409`), and anything PostgREST reports, mapped from
`SupabaseError.category`.

## Stack & notes

- **Kotlin 2.3** · **Spring Boot 3.5** · **Java 21**
- **Metro DI 1.1.1** (`dev.zacsweers.metro`)
- **supabase-kmp 0.9.1** — `supabase-client`, `supabase-database`
- springdoc-openapi · kotlinx-coroutines / serialization

> Kotlin is pinned to **2.3** on purpose — it's the version Metro 1.1.1 targets,
> and a 2.3 compiler can still read supabase-kmp's 2.4.0 metadata (a compiler
> reads one minor version of metadata ahead).

## Credits

Data layer powered by [supabase-kmp](https://github.com/AndroidPoet/supabase-kmp).

## License

[MIT](LICENSE)
