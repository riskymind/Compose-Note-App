package com.asterisk.notesapplication

import android.app.Application
import com.asterisk.notesapplication.di.DependencyInjector

/**
 * Application class responsible for initializing the dependency injector.
 */
class NotesApplication : Application() {

    lateinit var di: DependencyInjector

    override fun onCreate() {
        super.onCreate()
        di = DependencyInjector(this)
    }
}