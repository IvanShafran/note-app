package com.github.ivanshafran.noteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteListActivity : AppCompatActivity(), NoteListAdapter.OnClickListener {

    private val noteListAdapter = NoteListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = noteListAdapter

        createNoteButton.setOnClickListener {
            startActivity(CameraActivity.getIntent(this))
        }

        lifecycleScope.launch {
            val notes = withContext(Dispatchers.Default) {
                database.noteDao().getAll()
            }
            noteListAdapter.setNotes(notes)
        }
    }

    override fun onClick(id: Long) {

    }

}
