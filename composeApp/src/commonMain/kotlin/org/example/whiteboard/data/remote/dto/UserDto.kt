package org.example.whiteboard.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    @SerialName("_id")
    val id: String,
    val username: String,
    val roomIds: List<String>?,
    val refreshToken: String
)
