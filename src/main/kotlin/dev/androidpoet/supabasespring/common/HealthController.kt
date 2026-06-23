package dev.androidpoet.supabasespring.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/livez")
    fun livez(): String = "ok"

    @GetMapping("/readyz")
    fun readyz(): String = "ok"
}
