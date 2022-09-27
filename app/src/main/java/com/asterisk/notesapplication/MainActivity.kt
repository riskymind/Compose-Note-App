package com.asterisk.notesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asterisk.notesapplication.routing.NotesRouter
import com.asterisk.notesapplication.routing.Screen
import com.asterisk.notesapplication.ui.screens.NotesScreen
import com.asterisk.notesapplication.ui.screens.SaveNoteScreen
import com.asterisk.notesapplication.ui.screens.TrashScreen
import com.asterisk.notesapplication.ui.theme.NotesApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainActivityScreen()
                }
            }
        }
    }

    @Composable
    private fun MainActivityScreen() {
        when (NotesRouter.currentScreen) {
            Screen.Notes -> NotesScreen()
            Screen.SaveNote -> SaveNoteScreen()
            Screen.Trash -> TrashScreen()
        }
    }
}

