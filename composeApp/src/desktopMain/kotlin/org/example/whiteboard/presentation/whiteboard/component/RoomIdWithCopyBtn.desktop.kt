package org.example.whiteboard.presentation.whiteboard.component

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun copyRoomId(roomId: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(roomId)
    clipboard.setContents(selection, null)
}