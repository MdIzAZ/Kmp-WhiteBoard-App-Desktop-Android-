package org.example.whiteboard.data.converter

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathParser
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class PathConverter {

    @TypeConverter
    fun fromPath(path: Path): String {
        return serializePath(path)
    }

    @TypeConverter
    fun toPath(pathString: String): Path {
        return deserializePath(pathString)
    }


    private fun serializePath(path: Path): String {
        val pathString = StringBuilder()
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val length = pathMeasure.length
        var distance = 0f
        while (distance < length) {
            val pos = pathMeasure.getPosition(distance)
            if (distance == 0f) {
                pathString.append("M${pos.x},${pos.y}")
            } else {
                pathString.append("L${pos.x},${pos.y}")
            }
            distance += 1f
        }
//        pathString.append("Z")
        return pathString.toString()
    }

    private fun deserializePath(pathString: String): Path {
        val pathParser = PathParser().parsePathString(pathString)
        return pathParser.toPath()
    }
}