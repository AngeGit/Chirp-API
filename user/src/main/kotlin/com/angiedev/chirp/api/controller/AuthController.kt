package com.angiedev.chirp.api.controller

import com.angiedev.chirp.api.dto.AuthenticatedUserDTO
import com.angiedev.chirp.api.dto.LoginRequestDTO
import com.angiedev.chirp.api.dto.RegisterRequestDTO
import com.angiedev.chirp.api.dto.UserDTO
import com.angiedev.chirp.api.mapper.toAuthenticatedUserDTO
import com.angiedev.chirp.api.mapper.toUserDTO
import com.angiedev.chirp.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequestDTO
    ): UserDTO =
        authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toUserDTO()

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequestDTO
    ): AuthenticatedUserDTO  = authService.login(
        email = body.email,
        password = body.password
    ).toAuthenticatedUserDTO()
}