package org.example.whiteboard.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.whiteboard.data.util.Constants.DATASTORE_FILE_NAME

fun dataStore(context: Context): DataStore<Preferences> {
    return createDataStore(
        producePath = { context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath }
    )
}