package com.leaf.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leaf.notes.R
import com.leaf.notes.model.Note

class NoteAdapter(private val data: ArrayList<Note>) : RecyclerView.Adapter<NoteViewHolder>() {
    private var onClickListener: ((Int) -> Unit)? = null
    private var onLongClickListener: ((Int) -> Unit)? = null

    fun setOnClickListener(clickListener: (Int) -> Unit) {
        onClickListener = clickListener
    }
    fun setOnLongClickListener(clickListener: (Int) -> Unit) {
        onLongClickListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view, onClickListener, onLongClickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(data[position])
}
class NoteViewHolder(itemView: View,
                     private val onClickListener: ((Int) -> Unit)?, private val onLongClickListener: ((Int) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
    private val layout:FrameLayout = itemView.findViewById(R.id.noteLayout)
    private val date: TextView = itemView.findViewById(R.id.text_name)
    private val name: TextView = itemView.findViewById(R.id.text_note)

    fun bind(note: Note) {
        date.text = "09/21"
        name.text = note.name
        layout.setOnClickListener {
            onClickListener?.invoke(bindingAdapterPosition)
        }
        layout.setOnLongClickListener {
            onLongClickListener?.invoke(bindingAdapterPosition)
            return@setOnLongClickListener true
        }
    }
}