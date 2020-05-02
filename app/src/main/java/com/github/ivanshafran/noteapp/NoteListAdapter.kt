package com.github.ivanshafran.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NoteViewHolder(
    view: View,
    listener: NoteListAdapter.OnClickListener
) : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            note?.let { listener.onClick(it.id) }
        }
    }

    private val imageView = view.findViewById<ImageView>(R.id.noteImageView)
    private val textView = view.findViewById<TextView>(R.id.noteTextView)
    private var note: Note? = null

    fun bind(note: Note) {
        this.note = note
        Picasso.get()
            .load(note.imageFilePath)
            .into(imageView)
        textView.text = note.text
    }

}

private class NoteDiffCallback(
    private val oldNotes: List<Note>,
    private val newNotes: List<Note>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes[oldItemPosition].id == newNotes[newItemPosition].id
    }

    override fun getOldListSize() = oldNotes.size
    override fun getNewListSize() = newNotes.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes[oldItemPosition].modificationDate ==
                newNotes[newItemPosition].modificationDate
    }
}

class NoteListAdapter(
    private val listener: OnClickListener
) : RecyclerView.Adapter<NoteViewHolder>() {

    interface OnClickListener {
        fun onClick(id: Long)
    }

    private var notes: List<Note> = emptyList()

    fun setNotes(notes: List<Note>) {
        val result = DiffUtil.calculateDiff(NoteDiffCallback(this.notes, notes), false)
        this.notes = notes
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
        return NoteViewHolder(view, listener)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }
}
