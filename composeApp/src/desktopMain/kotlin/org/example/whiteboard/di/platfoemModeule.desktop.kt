package org.example.whiteboard.di


import org.example.whiteboard.data.datastore.createDataStore
import org.example.whiteboard.data.database.getRoomDatabaseBuilder
import org.example.whiteboard.data.util.Constants.DATASTORE_FILE_NAME
import org.koin.dsl.module

actual val  platformModule = module {

    single { getRoomDatabaseBuilder() }

    single { createDataStore { DATASTORE_FILE_NAME } }

//    factory {
//        SavedStateHandle(mapOf("whiteboardId" to null))
//    }

}

