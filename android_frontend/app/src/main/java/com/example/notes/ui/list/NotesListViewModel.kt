package com.example.notes.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class)
class NotesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = NotesRepository.getInstance(application)

    private val query = MutableStateFlow("")
    private val dataFlow = query
        .debounce(250)
        .flatMapLatest { q ->
            if (q.isBlank()) repo.observeAll() else repo.observeSearch(q)
        }

    val notes: StateFlow<List<Note>> = dataFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // PUBLIC_INTERFACE
    /** Update the search query text. */
    fun setQuery(text: String) {
        query.value = text
    }
}
