package org.example.whiteboard.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.whiteboard.data.util.Constants.DATABASE_NAME


fun getRoomDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDataBase> {

    val dbFile = context.getDatabasePath(DATABASE_NAME)

    return Room.databaseBuilder<AppDataBase>(
        context = context,
        name = dbFile.absolutePath
    )
}