package org.example.whiteboard.domain.model

sealed class Mode() {
    data class Online(val roomId: String) : Mode()
    data object Offline : Mode()
}