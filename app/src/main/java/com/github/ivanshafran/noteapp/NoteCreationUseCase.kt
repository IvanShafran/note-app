package com.github.ivanshafran.noteapp

import android.content.Context
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class NoteCreationUseCase(private val context: Context) {

    suspend fun createNoteFromImage(file: File): Long {
        val image = withContext(Dispatchers.IO) {
            FirebaseVisionImage.fromFilePath(context, Uri.fromFile(file))
        }

        val text = withContext(Dispatchers.Default) {
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
            detector.processImage(image).await().text
        }

        return withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val note = Note(
                id = 0,
                text = text,
                imageFilePath = file.absolutePath,
                creationDate = calendar,
                modificationDate = calendar
            )
            database.noteDao().insert(note)
        }
    }

}
