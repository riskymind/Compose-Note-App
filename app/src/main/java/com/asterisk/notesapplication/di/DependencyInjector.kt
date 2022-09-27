package com.asterisk.notesapplication.di

import android.content.Context
import androidx.room.Room
import com.asterisk.notesapplication.data.database.AppDatabase
import com.asterisk.notesapplication.data.database.dbmapper.DbMapper
import com.asterisk.notesapplication.data.database.dbmapper.DbMapperImpl
import com.asterisk.notesapplication.utils.Constants.DATABASE_NAME

class DependencyInjector(context: Context) {

    private val database: AppDatabase by lazy { provideDatabase(context) }

    private val dbMapper: DbMapper = DbMapperImpl()

    private fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

}