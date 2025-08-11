package org.example.whiteboard.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val message: String,
    val token: String? = null,
    val user: UserDto? = null
)
