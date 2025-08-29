package org.example.whiteboard.data.remote.apis

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerializationException
import org.example.whiteboard.data.remote.apis.RemoteDbApi.Companion.BASE_URL
import org.example.whiteboard.data.remote.dto.AuthRequest
import org.example.whiteboard.data.remote.dto.AuthResponse

class AuthenticationAPI(
    private val httpClient: HttpClient
) {

    companion object {
//        const val BASE_URL = "http://localhost:8000"
        const val BASE_URL = "https://whiteboard-backend-vcgb.onrender.com"
    }

    suspend fun loginUser(authRequest: AuthRequest): AuthResponse? {
        return try {
            httpClient.post("${BASE_URL}/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(authRequest)
            }.body<AuthResponse>()

        } catch (e: ClientRequestException) {
            // HTTP 4xx/5xx with a body
            println("Client error: ${e.response.status}")
            null
        } catch (e: SerializationException) {
            // Body doesn't match AuthResponse
            println("Parsing failed: ${e.message}")
            null
        } catch (e: Exception) {
            println("Unexpected error: ${e.message}")
            null
        }
    }

    suspend fun registerUser(authRequest: AuthRequest): AuthResponse? {
        return try {
            httpClient.post("${BASE_URL}/api/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(authRequest)
            }.body<AuthResponse>()

        } catch (e: ClientRequestException) {
            // HTTP 4xx/5xx with a body
            println("Client error: ${e.response.status}")
            null
        } catch (e: SerializationException) {
            // Body doesn't match AuthResponse
            println("Parsing failed: ${e.message}")
            null
        } catch (e: Exception) {
            println("Unexpected error: ${e.message}")
            null
        }
    }


}