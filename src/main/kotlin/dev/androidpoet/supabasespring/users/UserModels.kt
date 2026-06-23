package dev.androidpoet.supabasespring.users

import dev.androidpoet.supabasespring.common.ValidationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A row of the `users` table (decoded by supabase-kmp via kotlinx.serialization). */
@Serializable
data class User(
    val id: String,
    val email: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("created_at") val createdAt: String? = null,
)

/** Insert payload — `created_at` is filled by a column default. */
@Serializable
data class NewUser(
    val id: String,
    val email: String,
    @SerialName("display_name") val displayName: String,
)

/** Validated, normalised intent to create a user. */
data class CreateUserCommand(
    val email: String,
    val displayName: String,
)

/** Inbound request body (Jackson). Validates and normalises into a command. */
data class CreateUserRequest(
    val email: String = "",
    val displayName: String = "",
) {
    fun toCommand(): CreateUserCommand {
        val errors =
            buildMap {
                if (email.isBlank()) put("email", "Email is required")
                else if (!email.contains("@")) put("email", "Email must be valid")
                else if (email.length > 320) put("email", "Email must be 320 characters or fewer")
                if (displayName.isBlank()) put("displayName", "Display name is required")
                else if (displayName.length > 120) put("displayName", "Display name must be 120 characters or fewer")
            }
        if (errors.isNotEmpty()) throw ValidationException(errors)

        return CreateUserCommand(
            email = email.trim().lowercase(),
            displayName = displayName.trim(),
        )
    }
}

/** Outbound representation (Jackson). */
data class UserResponse(
    val id: String,
    val email: String,
    val displayName: String,
    val createdAt: String?,
)

fun User.toResponse() =
    UserResponse(
        id = id,
        email = email,
        displayName = displayName,
        createdAt = createdAt,
    )
