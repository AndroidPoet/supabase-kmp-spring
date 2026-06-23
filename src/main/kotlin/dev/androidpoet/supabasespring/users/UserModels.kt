package dev.androidpoet.supabasespring.users

import dev.androidpoet.supabasespring.common.ValidationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class NewUser(
    val id: String,
    val email: String,
    @SerialName("display_name") val displayName: String,
)

data class CreateUserCommand(
    val email: String,
    val displayName: String,
)

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
