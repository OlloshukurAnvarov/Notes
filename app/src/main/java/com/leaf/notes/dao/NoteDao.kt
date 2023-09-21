package com.leaf.notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leaf.notes.model.Note

@Dao
interface NoteDao {
    @Insert
    fun addNote(note: Note)

    @Query("SELECT * FROM notes")
    fun notes(): List<Note>

    @Query("UPDATE notes SET name = :name, note = :note WHERE id = :id")
    fun editNote(id: Long, name: String, note: String)

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteNote(id: Long)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Long) : Note
}