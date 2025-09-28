package com.angiedev.chirp.domain.exception

class InvalidTokenException(
    override val message: String?
): RuntimeException(message ?: "The provided token is invalid or has expired"){
    companion object{
        const val CODE = "INVALID_TOKEN"
        const val DEFAULT_MESSAGE = "The provided token is invalid or has expired"
        const val INVALID_TOKEN_MESSAGE = "The attached token is invalid"
    }
}
