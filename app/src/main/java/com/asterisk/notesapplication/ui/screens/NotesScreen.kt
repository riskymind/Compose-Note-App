package com.asterisk.notesapplication.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.routing.Screen
import com.asterisk.notesapplication.ui.components.AppDrawer
import com.asterisk.notesapplication.ui.components.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotesScreen() {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notes", color = MaterialTheme.colors.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(imageVector = Icons.Filled.List, contentDescription = "Drawer Button")
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(currentScreen = Screen.Notes) {
                coroutineScope.launch { scaffoldState.drawerState.close() }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {}, contentColor = MaterialTheme.colors.background) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Note Button")
            }
        },
        content = {}
    )
}


@Composable
fun NoteList(
    notes: List<NoteModel>,
    onNoteCheckedChange: (NoteModel) -> Unit,
    onNoteClicked: (NoteModel) -> Unit,
) {

    LazyColumn {
        items(count = notes.size) { noteIndex ->
            val note = notes[noteIndex]
            Note(note = note,
                isSelected = false,
                onNoteClick = onNoteClicked,
                onNoteCheckedChange = onNoteCheckedChange)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    NoteList(
        notes = listOf(
            NoteModel(1, "Note 1", "Content 1", null),
            NoteModel(2, "Note 2", "Content 2", false),
            NoteModel(3, "Note 3", "Content 3", true)
        ),
        onNoteCheckedChange = {},
        onNoteClicked = {})
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen()
}