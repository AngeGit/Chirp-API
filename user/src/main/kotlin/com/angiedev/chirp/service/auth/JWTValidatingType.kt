package com.angiedev.chirp.service.auth

enum class JWTValidatingType(val type: String) {
    ACCESS("access"), REFRESH("refresh");
}