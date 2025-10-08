package com.example.notes.ui.list.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.data.Note
import com.example.notes.databinding.ItemNoteBinding
import java.util.Date

class NotesAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemNoteBinding,
        private val onClick: (Note) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.title.text = if (item.title.isBlank()) "(Untitled)" else item.title
            val preview = item.content.lines().firstOrNull().orEmpty()
            binding.content.text = preview
            val date = Date(item.updatedAt)
            binding.date.text = DateFormat.getMediumDateFormat(binding.root.context).format(date) + " " +
                    DateFormat.getTimeFormat(binding.root.context).format(date)
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
        }
    }
}
