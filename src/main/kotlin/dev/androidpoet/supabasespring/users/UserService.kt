package dev.androidpoet.supabasespring.users

import dev.androidpoet.supabasespring.common.ConflictException
import dev.androidpoet.supabasespring.common.NotFoundException
import dev.androidpoet.supabasespring.common.unwrap
import dev.androidpoet.supabasespring.di.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.runBlocking
import java.util.UUID

@Inject
@SingleIn(AppScope::class)
class UserService(
    private val users: UserRepository,
) {
    fun create(command: CreateUserCommand): User =
        runBlocking {
            if (users.existsByEmail(command.email).unwrap()) {
                throw ConflictException("A user already exists with email: ${command.email}")
            }
            users
                .save(
                    NewUser(
                        id = UUID.randomUUID().toString(),
                        email = command.email,
                        displayName = command.displayName,
                    ),
                ).unwrap()
        }

    fun findById(id: String): User =
        runBlocking {
            users.findById(id).unwrap() ?: throw NotFoundException("User not found: $id")
        }
}
