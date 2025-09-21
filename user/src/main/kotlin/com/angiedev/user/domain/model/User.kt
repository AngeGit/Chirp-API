package com.angiedev.user.domain.model

import java.util.UUID

typealias UserId = UUID

data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val hasEmailVerified: Boolean,
)
