package org.example.whiteboard.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
