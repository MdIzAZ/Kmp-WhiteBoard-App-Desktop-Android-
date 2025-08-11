package org.example.whiteboard.di

import org.example.whiteboard.data.datastore.dataStore
import org.example.whiteboard.data.database.getRoomDatabaseBuilder
import org.koin.dsl.module

actual val platformModule = module {

    single {
        getRoomDatabaseBuilder(get())
    }

    single {
        dataStore(get())
    }
}