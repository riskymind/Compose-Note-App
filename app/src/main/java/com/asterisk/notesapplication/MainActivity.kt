package com.asterisk.notesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.asterisk.notesapplication.viewmodel.MainViewModel
import com.asterisk.notesapplication.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        MainViewModelFactory(this, (application as NotesApplication).di.repository)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainActivityScreen(viewModel)
                }
            }
        }
    }

    @Composable
    private fun MainActivityScreen(viewModel: MainViewModel) {
        when (NotesRouter.currentScreen) {
            Screen.Notes -> NotesScreen(viewModel)
            Screen.SaveNote -> SaveNoteScreen()
            Screen.Trash -> TrashScreen()
        }
    }
}

