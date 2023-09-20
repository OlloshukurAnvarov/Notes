package com.leaf.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
class Note(
    @PrimaryKey(true)
    val id: Long,
    val note: String
)