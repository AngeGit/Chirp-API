package com.angiedev.chirp.api.dto

import com.angiedev.chirp.domain.model.UserId

data class UserDTO(
    val id: UserId,
    val name: String,
    val email: String,
    val hasEmailVerified: Boolean,
)
