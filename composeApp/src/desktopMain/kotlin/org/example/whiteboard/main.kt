package org.example.whiteboard

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.whiteboard.di.initKoin
import org.example.whiteboard.presentation.App

fun main() {

    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "White Board",
        ) {
            App()
        }
    }

}