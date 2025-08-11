package org.example.whiteboard.domain.repo

import org.example.whiteboard.data.remote.dto.AuthRequest
import org.example.whiteboard.data.remote.dto.AuthResponse

interface AuthRepo {

    suspend fun login(authRequest: AuthRequest): AuthResponse?

    suspend fun register(authRequest: AuthRequest): AuthResponse?
}