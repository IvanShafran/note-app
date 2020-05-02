package com.github.ivanshafran.noteapp

import androidx.room.*
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        return value?.let { Calendar.getInstance().apply { time = Date(value) } }
    }

    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar?): Long? {
        return calendar?.time?.time
    }
}

@TypeConverters(Converters::class)
@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

val database by lazy {
    Room.databaseBuilder(App.context, AppDatabase::class.java, "database").build()
}
