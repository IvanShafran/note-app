package com.github.ivanshafran.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteListActivity : AppCompatActivity(), NoteListAdapter.OnClickListener {

    companion object {
        private const val NEW_NOTE_REQUEST_CODE = 0
    }

    private val noteListAdapter = NoteListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = noteListAdapter

        createNoteButton.setOnClickListener {
            startActivityForResult(CameraActivity.getIntent(this), NEW_NOTE_REQUEST_CODE)
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val notes = withContext(Dispatchers.Default) {
                database.noteDao().getAll()
            }
            noteListAdapter.setNotes(notes)
        }
    }

    override fun onClick(id: Long) {
        openDetailNoteScreen(id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            NEW_NOTE_REQUEST_CODE -> onNewNoteRequestResult(resultCode, data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onNewNoteRequestResult(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        val id = CameraActivity.getNoteIdFromData(data)
        openDetailNoteScreen(id)
    }

    private fun openDetailNoteScreen(id: Long) {

    }

}
