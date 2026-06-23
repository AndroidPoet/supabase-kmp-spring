plugins {
	kotlin("jvm") version "2.3.0"
	kotlin("plugin.spring") version "2.3.0"
	kotlin("plugin.serialization") version "2.3.0"
	id("org.springframework.boot") version "3.5.15"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.androidpoet.supabasespring"
version = "0.0.1-SNAPSHOT"

// Align Spring Boot's managed Kotlin/coroutines versions with our 2.3.0 compiler.
extra["kotlin.version"] = "2.3.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

val supabaseKmp = "0.9.1"

dependencies {
	// Spring Boot (web + JSON)
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// supabase-kmp (AndroidPoet) — JVM artifacts, only the modules we use.
	implementation("io.github.androidpoet:supabase-client:$supabaseKmp")
	implementation("io.github.androidpoet:supabase-database:$supabaseKmp")

	// supabase-kmp is coroutine-first; bridge to Spring MVC with runBlocking.
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

	// Auto-generated OpenAPI document + Swagger UI.
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
