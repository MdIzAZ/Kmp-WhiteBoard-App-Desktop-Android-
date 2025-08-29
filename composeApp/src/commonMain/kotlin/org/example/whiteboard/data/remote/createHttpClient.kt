package org.example.whiteboard.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.example.whiteboard.data.remote.dto.RefreshTokenResponse
import org.example.whiteboard.domain.repo.SettingsRepo


val refreshClient = HttpClient {

}
fun createHttpClient(settingsRepo: SettingsRepo): HttpClient {

    val client =  HttpClient {

        install(HttpTimeout) {
            requestTimeoutMillis = 120_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 120_000
        }

        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {

                    val accessToken = settingsRepo.getAccessToken().firstOrNull()
                    val refreshToken = settingsRepo.getRefreshToken().firstOrNull()

                    println("Token : $accessToken \n Refresh Token : $refreshToken")
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else null
                }

                refreshTokens {
                    val refreshToken = settingsRepo.getRefreshToken().firstOrNull()
                    if (refreshToken != null) {
                        try {

                            val response: RefreshTokenResponse = refreshClient.post {
                                url("http://localhost:8000/api/auth/refresh-token")
                                contentType(ContentType.Application.Json)
                                setBody(refreshToken)
                            }.body()


                            settingsRepo.saveAccessToken(response.accessToken)

                            BearerTokens(response.accessToken, refreshToken)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    } else null
                }


            }
        }
    }

    return client
}
