package org.example.whiteboard.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers


fun getRoomDatabase(
    databaseBuilder: RoomDatabase.Builder<AppDataBase>
): AppDataBase {

    return databaseBuilder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

}