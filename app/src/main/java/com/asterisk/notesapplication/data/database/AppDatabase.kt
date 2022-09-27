package com.asterisk.notesapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asterisk.notesapplication.data.database.dao.ColorDao
import com.asterisk.notesapplication.data.database.dao.NoteDao
import com.asterisk.notesapplication.data.database.model.ColorDbModel
import com.asterisk.notesapplication.data.database.model.NoteDbModel

@Database(entities = [NoteDbModel::class, ColorDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun colorDao(): ColorDao
}