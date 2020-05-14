package com.github.ivanshafran.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_note_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteListFragment : Fragment(R.layout.fragment_note_list), NoteListAdapter.OnClickListener {

    companion object {
        private const val NEW_NOTE_REQUEST_CODE = 0

        fun newInstance() = NoteListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteListAdapter = NoteListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = noteListAdapter

        createNoteButton.setOnClickListener {
            startActivityForResult(
                CameraActivity.getIntent(requireContext()),
                NEW_NOTE_REQUEST_CODE
            )
        }

        lifecycleScope.launch {
            val notes = withContext(Dispatchers.IO) {
                database.noteDao().getAll()
            }
            noteListAdapter.setNotes(notes)
        }
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

    override fun onClick(id: Long) {
        openDetailNoteScreen(id)
    }

    private fun openDetailNoteScreen(id: Long) {
        (activity as? MainActivity)?.openDetailNoteScreen(id)
    }
}
