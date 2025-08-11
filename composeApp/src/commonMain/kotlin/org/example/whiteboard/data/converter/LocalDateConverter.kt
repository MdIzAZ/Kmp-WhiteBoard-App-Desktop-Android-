package org.example.whiteboard.data.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

class LocalDateConverter {

    private val sortableDateFormat = LocalDate.Format {
        year()
        chars("/")
        monthNumber()
        chars("/")
        dayOfMonth()
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(sortableDateFormat)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(input = dateString, format = sortableDateFormat)
    }
}