package dev.androidpoet.supabasespring.config

data class AppConfig(
    val supabase: SupabaseConfig,
) {
    companion object {
        fun fromEnvironment(env: Map<String, String> = System.getenv()) =
            AppConfig(
                supabase =
                    SupabaseConfig(
                        url = env["SUPABASE_URL"] ?: "http://localhost:54321",
                        anonKey = env["SUPABASE_ANON_KEY"] ?: "public-anon-key",
                        logging = env["SUPABASE_LOGGING"]?.toBoolean() ?: false,
                    ),
            )
    }
}

data class SupabaseConfig(
    val url: String,
    val anonKey: String,
    val logging: Boolean,
)
