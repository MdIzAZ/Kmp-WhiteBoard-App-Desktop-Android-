package org.example.whiteboard.data.remote.apis

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import org.example.whiteboard.data.remote.apis.AuthenticationAPI.Companion.BASE_URL
import org.example.whiteboard.data.remote.dto.PathsResponse
import org.example.whiteboard.domain.repo.SettingsRepo

class RemoteDbApi(
    private val httpClient: HttpClient,
    private val settingsRepo: SettingsRepo
) {

    companion object {
//        const val BASE_URL = "http://localhost:8000"
        const val BASE_URL = "https://whiteboard-backend-vcgb.onrender.com"
    }


    suspend fun deleteRoom(
        roomId: String,
        onResult: suspend (Boolean) -> Unit
    ) {

        try {
            val response = httpClient.delete("${BASE_URL}/api/whiteboard/$roomId")
            println(response)
            if (response.status.value in 200..299) {
                onResult(true)
            } else {
                println("Failed with status: ${response.status}")
                onResult(false)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            onResult(false)
        }

    }

    suspend fun fetchPathsForWhiteboard(
        roomId: String,
        onResult: (Boolean) -> Unit
    ): PathsResponse {

        return try {
            val response = httpClient.get("${BASE_URL}/api/whiteboard/$roomId/paths")
                .body<PathsResponse>()
            onResult(true)
            response
        } catch (e: Exception) {
            println("Error fetching paths: ${e.message}")
            onResult(false)
            PathsResponse(emptyList())
        }
    }












    suspend fun deleteAllPathsForRoom(
        roomId: String,
        onResult: (Boolean) -> Unit
    ) {

    }




}












































