package com.github.ivanshafran.noteapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    companion object {
        private const val NOTE_ID_KEY = "note_id"

        fun newInstance(id: Long): Fragment {
            val fragment = NoteDetailFragment()
            val arguments = Bundle()
            arguments.putLong(NOTE_ID_KEY, id)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = checkNotNull(arguments?.getLong(NOTE_ID_KEY)) { "Null note id" }
        lifecycleScope.launch {
            val note = withContext(Dispatchers.IO) {
                database.noteDao().getById(id)
            }
            if (note == null) {
                fragmentManager?.popBackStack()
                return@launch
            }
            if (!isActive) return@launch

            noteTextView.text = note.text
            Picasso.get()
                .load(File(note.imageFilePath))
                .fit()
                .centerInside()
                .into(noteImageView)
        }
    }

}
