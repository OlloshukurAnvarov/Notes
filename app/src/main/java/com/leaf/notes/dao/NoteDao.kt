package com.leaf.notes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.leaf.notes.model.Note

@Dao
interface NoteDao {
    @Insert
    fun addNote(note: Note)

    @Query("SELECT * FROM notes")
    fun notes(): List<Note>

    @Query("UPDATE notes SET note = :note WHERE id = :id")
    fun editNote(id: Long, note: String)

    @Delete
    fun deleteNote(id: Long)
}