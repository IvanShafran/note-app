package com.github.ivanshafran.noteapp

import java.util.*

data class Note(
    val id: Long,
    val imageFilePath: String,
    val text: String,
    val creationDate: Calendar
)
