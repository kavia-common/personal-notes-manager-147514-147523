package com.example.notes.ui.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = NotesRepository.getInstance(application)

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    // PUBLIC_INTERFACE
    /** Load a note by id if id > 0, else consider as new note. */
    fun load(noteId: Long) {
        if (noteId <= 0) {
            _note.value = null
            return
        }
        viewModelScope.launch {
            _note.value = repo.getById(noteId)
        }
    }

    // PUBLIC_INTERFACE
    /** Save the note (insert or update). Returns the id via callback. */
    fun save(noteId: Long, title: String, content: String, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val id = if (noteId > 0) {
                repo.update(noteId, title, content)
                noteId
            } else {
                repo.add(title, content)
            }
            onSaved(id)
        }
    }

    // PUBLIC_INTERFACE
    /** Delete the note if it exists. */
    fun delete(noteId: Long, onDeleted: () -> Unit) {
        if (noteId <= 0) return
        viewModelScope.launch {
            repo.delete(noteId)
            onDeleted()
        }
    }
}
