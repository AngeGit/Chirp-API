package com.angiedev.chirp.service.auth

import com.angiedev.chirp.domain.exception.UserAlreadyExistsException
import com.angiedev.chirp.domain.model.User
import com.angiedev.chirp.infra.database.entities.UserEntity
import com.angiedev.chirp.infra.database.mappers.toUser
import com.angiedev.chirp.infra.database.repositories.UserRepository
import com.angiedev.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(username: String, email: String, password: String): User {
        userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()
        )?.let {
            throw UserAlreadyExistsException()
        }
        return userRepository.save(
            UserEntity(
                username = username.trim(),
                email = email.trim(),
                hashedPassword = passwordEncoder.encode(password.trim())!!
            )
        ).toUser()
    }
}