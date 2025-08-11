package org.example.whiteboard.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.whiteboard.data.util.Constants.DATABASE_NAME
import java.io.File

fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AppDataBase> {

    val dbFile = File(System.getProperty("java.io.tmpdir"), DATABASE_NAME)

    return Room.databaseBuilder<AppDataBase>(name = dbFile.absolutePath)

}