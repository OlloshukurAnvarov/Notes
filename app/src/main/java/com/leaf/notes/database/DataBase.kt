package com.leaf.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leaf.notes.dao.NoteDao
import com.leaf.notes.model.Note

@Database(version = 1, entities = [Note::class])
abstract class DataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        private lateinit var database: DataBase

        fun getData(context: Context): DataBase {
            if (!::database.isInitialized)
                database = Room.databaseBuilder(context, DataBase::class.java, "Notes")
                    .allowMainThreadQueries()
                    .build()
            return database
        }

    }

}