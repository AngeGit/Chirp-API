package com.angiedev.chirp.service.auth


import com.angiedev.chirp.domain.exception.InvalidTokenException
import com.angiedev.chirp.domain.exception.InvalidTokenException.Companion.INVALID_TOKEN_MESSAGE
import com.angiedev.chirp.domain.model.UserId
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import kotlin.io.encoding.Base64

@Service
class JwtService(
    @param:Value("\${jwt.secret}") private val secretBase64: String,
    @param:Value("\${jwt.expiration-minutes}") private val expirationMinutes: Int,
) {
    val BEARER_PREFIX = "Bearer "
    private val secretKey = Keys.hmacShaKeyFor(
        Base64.decode(secretBase64)
    )
    private val accessTokenValidityInMillis = expirationMinutes * 60 * 1000L
    private val refreshTokenValidityInMillis = 60 * 24 * 30 * 60 * 1000L


    fun generateAccessToken(userId: UserId): String =
        generateToken(
            userId = userId,
            type = JWTValidatingType.ACCESS.type,
            expiry = accessTokenValidityInMillis
        )

    fun generateRefreshToken(userId: UserId): String =
        generateToken(
            userId = userId,
            type = JWTValidatingType.REFRESH.type,
            expiry = refreshTokenValidityInMillis
        )

    fun validateAccessToken(token: String): Boolean = validateToken(token, JWTValidatingType.ACCESS.type)

    fun validateRefreshToken(token: String): Boolean = validateToken(token, JWTValidatingType.REFRESH.type)

    fun getUserIdFromToken(token: String): UserId? {
        val claims = parseAllClaims(token) ?: throw InvalidTokenException(INVALID_TOKEN_MESSAGE)

        return UUID.fromString(claims.subject)
    }

    private fun generateToken(
        userId: UserId,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", type)
            .issuedAt(now)
            .expiration(Date(now.time + expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith(BEARER_PREFIX)) {
            token.removePrefix(BEARER_PREFIX)
        } else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    private fun validateToken(token: String, validatingType: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val type = claims["type"] as? String ?: return false
        val isNotExpired = claims.expiration.after(Date())
        return type == validatingType && isNotExpired
    }
}