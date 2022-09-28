package com.asterisk.notesapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.asterisk.notesapplication.R
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.routing.Screen
import com.asterisk.notesapplication.ui.components.AppDrawer
import com.asterisk.notesapplication.ui.components.Note
import com.asterisk.notesapplication.utils.Constants.NO_DIALOG
import com.asterisk.notesapplication.utils.Constants.PERMANENTLY_DELETE_DIALOG
import com.asterisk.notesapplication.utils.Constants.RESTORE_NOTES_DIALOG
import com.asterisk.notesapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun TrashScreen(viewModel: MainViewModel) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val dialogState: MutableState<Int> = rememberSaveable { mutableStateOf(NO_DIALOG) }

    val notesInTrash: List<NoteModel> by viewModel.notesInTrash.observeAsState(listOf())

    val selectedNotes by viewModel.selectedNotes.observeAsState(listOf())

    Scaffold(
        topBar = {

            TrashTopAppBar(
                onNavigationIconClick = { coroutineScope.launch { scaffoldState.drawerState.open() } },
                onRestoreNotesClick = { dialogState.value = RESTORE_NOTES_DIALOG },
                onDeleteNotesClick = { dialogState.value = PERMANENTLY_DELETE_DIALOG },
                areActionsVisible = selectedNotes.isNotEmpty()
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(currentScreen = Screen.Trash) {
                coroutineScope.launch { scaffoldState.drawerState.close() }
            }
        }
    ) {
        Content(notes = notesInTrash,
            selectedNotes = selectedNotes,
            onNoteClick = { viewModel.onNoteSelected(it) })

        val dialog = dialogState.value

        if (dialog != NO_DIALOG) {
            val confirmAction: () -> Unit = when (dialog) {
                RESTORE_NOTES_DIALOG -> {
                    {
                        viewModel.restoreNotes(selectedNotes)
                        dialogState.value = NO_DIALOG
                    }
                }
                PERMANENTLY_DELETE_DIALOG -> {
                    {
                        viewModel.deleteNotesPermanently(selectedNotes)
                        dialogState.value = NO_DIALOG
                    }
                }
                else -> {
                    {
                        dialogState.value = NO_DIALOG
                    }
                }
            }

            AlertDialog(
                onDismissRequest = { dialogState.value = NO_DIALOG },
                title = { Text(text = mapDialogTitle(dialog)) },
                text = { Text(text = mapDialogText(dialog)) },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dialogState.value = NO_DIALOG }) {
                        Text(text = "Dismiss")
                    }
                }
            )
        }
    }

}

fun mapDialogText(dialog: Int): String {
    return when (dialog) {
        RESTORE_NOTES_DIALOG -> "Are you sure you want to restore selected notes"
        PERMANENTLY_DELETE_DIALOG -> "Are you sure you want to permanently delete selected notes"
        else -> throw RuntimeException("Dialog not supported $dialog")
    }
}

fun mapDialogTitle(dialog: Int): String {
    return when (dialog) {
        RESTORE_NOTES_DIALOG -> "Restore Notes"
        PERMANENTLY_DELETE_DIALOG -> "Delete notes forever"
        else -> throw RuntimeException("Dialog not supported $dialog")
    }
}


@Composable
fun TrashTopAppBar(
    onNavigationIconClick: () -> Unit,
    onRestoreNotesClick: () -> Unit,
    onDeleteNotesClick: () -> Unit,
    areActionsVisible: Boolean,
) {

    TopAppBar(
        title = { Text(text = "Trash") },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Filled.List, contentDescription = "Drawer button")
            }
        },
        actions = {
            if (areActionsVisible) {
                IconButton(onClick = onRestoreNotesClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_restore),
                        contentDescription = "Restore bubtton",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = onDeleteNotesClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete bubtton",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )

}

@Composable
fun Content(
    notes: List<NoteModel>,
    onNoteClick: (NoteModel) -> Unit,
    selectedNotes: List<NoteModel>,
) {

    val tabs = listOf("REGULAR", "CHECKABLE")

    // Initial state for selected tab
    var selectedTab by remember { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title, fontStyle = FontStyle.Italic) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        // filter notes Checkable || Not
        val filteredNotes = when (selectedTab) {
            0 -> {
                notes.filter { it.isCheckOff == null }
            }
            1 -> {
                notes.filter { it.isCheckOff != null }
            }
            else -> throw IllegalStateException("Tab not supported indes: $selectedTab")
        }

        LazyColumn {
            items(filteredNotes.size) { index ->
                val note = filteredNotes[index]
                val isSelected = selectedNotes.contains(note)
                Note(note = note, isSelected = isSelected, onNoteClick = onNoteClick)
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TrashTopAppBarPreview() {
    TrashTopAppBar({}, {}, {}, true)
}