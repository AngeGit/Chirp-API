package com.angiedev.chirp.api.mapper

import com.angiedev.chirp.api.dto.AuthenticatedUserDTO
import com.angiedev.chirp.api.dto.UserDTO
import com.angiedev.chirp.domain.model.AuthenticatedUser
import com.angiedev.chirp.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDTO(): AuthenticatedUserDTO = AuthenticatedUserDTO(
        user = this.user.toUserDTO(),
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
fun User.toUserDTO(): UserDTO = UserDTO(
        id = this.id,
        name = this.name,
        email = this.email,
        hasEmailVerified = this.hasEmailVerified,
    )

