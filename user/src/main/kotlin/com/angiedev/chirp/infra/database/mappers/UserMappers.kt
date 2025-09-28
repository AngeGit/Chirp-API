package com.angiedev.chirp.infra.database.mappers

import com.angiedev.chirp.domain.model.User
import com.angiedev.chirp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User = User(
    id = id!!, // id is non-null because it's a primary key and should be set
    name = username,
    email = email,
    hasEmailVerified = hasVerifiedEmail,
)