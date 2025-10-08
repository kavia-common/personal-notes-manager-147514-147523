package com.example.notes.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class NotesRepository private constructor(context: Context) {

    private val dao = NotesDatabase.getInstance(context).noteDao()

    fun observeAll(): Flow<List<Note>> = dao.observeAll()

    fun observeSearch(query: String): Flow<List<Note>> = dao.observeSearch("%$query%")

    suspend fun getById(id: Long): Note? = dao.getById(id)

    suspend fun add(title: String, content: String): Long {
        val now = System.currentTimeMillis()
        return dao.insert(
            Note(
                title = title,
                content = content,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun update(id: Long, title: String, content: String) {
        val existing = dao.getById(id) ?: return
        dao.update(
            existing.copy(
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun delete(id: Long) {
        val note = dao.getById(id) ?: return
        dao.delete(note)
    }

    companion object {
        @Volatile private var INSTANCE: NotesRepository? = null

        // PUBLIC_INTERFACE
        /**
         * Provide singleton NotesRepository.
         */
        fun getInstance(context: Context): NotesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotesRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
