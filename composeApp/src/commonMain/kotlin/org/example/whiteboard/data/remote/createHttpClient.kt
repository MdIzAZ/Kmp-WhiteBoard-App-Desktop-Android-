package org.example.whiteboard.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.example.whiteboard.domain.repo.SettingsRepo

fun createHttpClient(settingsRepo: SettingsRepo): HttpClient {

    return HttpClient {
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
                    val token = settingsRepo.getAuthToken().firstOrNull()
                    println("Token : $token")
                    token?.let {
                        BearerTokens(accessToken = it, refreshToken = "")
                    }
                }

                refreshTokens {
                    null
                }
            }
        }
    }
}
