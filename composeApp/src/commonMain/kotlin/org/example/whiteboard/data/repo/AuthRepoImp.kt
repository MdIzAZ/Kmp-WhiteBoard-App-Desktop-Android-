package org.example.whiteboard.data.repo

import org.example.whiteboard.data.remote.AuthenticationAPI
import org.example.whiteboard.data.remote.dto.AuthRequest
import org.example.whiteboard.data.remote.dto.AuthResponse
import org.example.whiteboard.domain.repo.AuthRepo

class AuthRepoImp(
    private val authApi: AuthenticationAPI
): AuthRepo {

    override suspend fun login(authRequest: AuthRequest): AuthResponse? {
        return try {
            authApi.loginUser(authRequest)
        } catch (e: Exception) {
            println(e)
            null
        }

    }

    override suspend fun register(authRequest: AuthRequest): AuthResponse? {
        return try {
            authApi.registerUser(authRequest)
        } catch (e: Exception) {
            println(e)
            null
        }
    }

}