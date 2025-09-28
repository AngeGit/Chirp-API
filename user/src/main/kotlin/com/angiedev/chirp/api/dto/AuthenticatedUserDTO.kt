package com.angiedev.chirp.api.dto

data class AuthenticatedUserDTO(
    val user: UserDTO,
    val accessToken: String,
    val refreshToken: String,
)
