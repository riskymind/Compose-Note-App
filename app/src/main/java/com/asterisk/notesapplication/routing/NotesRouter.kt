package com.asterisk.notesapplication.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


/**
 *  Define all possible screen
 */
sealed class Screen {
    object Notes : Screen()
    object SaveNote : Screen()
    object Trash : Screen()
}


object NotesRouter {
    var currentScreen: Screen by mutableStateOf(Screen.Notes)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}