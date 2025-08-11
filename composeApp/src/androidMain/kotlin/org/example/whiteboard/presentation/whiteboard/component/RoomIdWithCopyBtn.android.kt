package org.example.whiteboard.presentation.whiteboard.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE


private lateinit var appContext: Context

fun initClipboardHelper(context: Context) {
    appContext = context.applicationContext
}

actual fun copyRoomId(roomId: String) {

    if (!::appContext.isInitialized) {
        throw IllegalStateException("Clipboard helper not initialized. Call initClipboardHelper() first.")
    }

    val clipboard = appContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Room Id", roomId)
    clipboard.setPrimaryClip(clip)
}