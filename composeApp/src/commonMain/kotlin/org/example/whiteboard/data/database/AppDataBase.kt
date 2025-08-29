package org.example.whiteboard.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.example.whiteboard.data.converter.LocalDateConverter
import org.example.whiteboard.data.converter.PathConverter
import org.example.whiteboard.data.database.dao.PathDao
import org.example.whiteboard.data.database.dao.WhiteboardDao
import org.example.whiteboard.data.database.entity.PathEntity
import org.example.whiteboard.data.database.entity.WhiteboardEntity


@Database(
    entities = [PathEntity::class, WhiteboardEntity::class],
    version = 16
)
@ConstructedBy(AppDataBaseConstructor::class)
@TypeConverters(PathConverter::class, LocalDateConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun pathDao(): PathDao

    abstract fun whiteboardDao(): WhiteboardDao



}