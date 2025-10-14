package com.angiedev.chirp.service.auth

import com.angiedev.chirp.domain.exception.InvalidCredentialsException
import com.angiedev.chirp.domain.exception.InvalidCredentialsException.Companion.EMAIL_NOT_FOUND_MESSAGE
import com.angiedev.chirp.domain.exception.InvalidCredentialsException.Companion.EMAIL_NOT_VERIFIED_MESSAGE
import com.angiedev.chirp.domain.exception.InvalidCredentialsException.Companion.PASSWORD_NOT_MATCH_MESSAGE
import com.angiedev.chirp.domain.exception.UserAlreadyExistsException
import com.angiedev.chirp.domain.exception.UserNotFoundException
import com.angiedev.chirp.domain.model.AuthenticatedUser
import com.angiedev.chirp.domain.model.User
import com.angiedev.chirp.domain.model.UserId
import com.angiedev.chirp.infra.database.entities.RefreshTokenEntity
import com.angiedev.chirp.infra.database.entities.UserEntity
import com.angiedev.chirp.infra.database.mappers.toUser
import com.angiedev.chirp.infra.database.repositories.RefreshTokenRepository
import com.angiedev.chirp.infra.database.repositories.UserRepository
import com.angiedev.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
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

    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {

        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialsException(EMAIL_NOT_FOUND_MESSAGE)

        if (!passwordEncoder.matches(password.trim(), user.hashedPassword)) {
            throw InvalidCredentialsException(PASSWORD_NOT_MATCH_MESSAGE)
        }

        /* TODO: Re-enable email verification check when email service is implemented
        if(!user.hasVerifiedEmail){
            throw InvalidCredentialsException(EMAIL_NOT_VERIFIED_MESSAGE)
        }*/
        return user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashed = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityInMillis
        val expiresAt = Instant.now().plusMillis(expiryMs)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiredAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}