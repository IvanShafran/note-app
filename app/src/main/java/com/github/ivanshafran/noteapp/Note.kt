package com.github.ivanshafran.noteapp

import androidx.room.*
import java.util.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "image_file_path") val imageFilePath: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "creation_date") val creationDate: Calendar,
    @ColumnInfo(name = "modification_date") val modificationDate: Calendar
)

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Note?

    @Insert
    suspend fun insert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)
}
