package com.angiedev.chirp.domain.exception

class InvalidCredentialsException(
    override val message: String?
): RuntimeException(message ?: DEFAULT_MESSAGE){
    companion object{
        const val CODE = "INVALID_CREDENTIALS"
        const val DEFAULT_MESSAGE = "The entered credentials are invalid"
        const val EMAIL_NOT_FOUND_MESSAGE = "The entered mail does not exist"
        const val PASSWORD_NOT_MATCH_MESSAGE = "The entered password doesn't match the email provided"
        const val EMAIL_NOT_VERIFIED_MESSAGE = "Email not verified. Please verify your email before logging in."
    }
}
