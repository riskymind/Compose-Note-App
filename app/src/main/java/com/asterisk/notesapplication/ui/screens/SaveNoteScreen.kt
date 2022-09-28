package com.asterisk.notesapplication.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asterisk.notesapplication.R
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.routing.NotesRouter
import com.asterisk.notesapplication.routing.Screen
import com.asterisk.notesapplication.ui.components.NoteColor
import com.asterisk.notesapplication.utils.Constants.DEFAULT
import com.asterisk.notesapplication.utils.Constants.NEW_NOTE_ID
import com.asterisk.notesapplication.utils.fromHex
import com.asterisk.notesapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SaveNoteScreen(viewModel: MainViewModel) {

    val noteEntry: NoteModel by viewModel.noteEntry.observeAsState(NoteModel())

    val colors: List<ColorModel> by viewModel.colors.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val moveNoteToTrashDialogShownState: MutableState<Boolean> =
        rememberSaveable { mutableStateOf(false) }


    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            NotesRouter.navigateTo(Screen.Notes)
        }
    }

    Scaffold(
        topBar = {
            // check if new or existing
            val isEditMode = noteEntry.id != NEW_NOTE_ID

            SaveNoteTopAppBar(
                isEditMode = isEditMode,
                onBackClick = { NotesRouter.navigateTo(Screen.Notes) },
                onSaveNoteClick = { viewModel.saveNote(noteEntry) },
                onOpenColorPickerClick = { coroutineScope.launch { bottomDrawerState.open() } },
                onDeleteClick = { moveNoteToTrashDialogShownState.value = true }
            )
        },
        content = {
            BottomDrawer(drawerContent = {
                ColorPicker(colors = colors, onColorSelect = { color ->
                    val newNote = noteEntry.copy(color = color)
                    viewModel.onNoteEntryChange(newNote)
                    coroutineScope.launch { bottomDrawerState.close() }
                })
            }, drawerState = bottomDrawerState, content = {
                SaveNoteContent(note = noteEntry,
                    onNoteChange = { viewModel.onNoteEntryChange(it) }, onColorClick = {
                        coroutineScope.launch { bottomDrawerState.open() }
                    })
            })

            if (moveNoteToTrashDialogShownState.value) {
                AlertDialog(
                    onDismissRequest = { moveNoteToTrashDialogShownState.value = false },
                    title = { Text(text = "Move note to trash") },
                    text = { Text(text = "Are you sure you want to move this note to trash?") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.moveNoteToTrash(noteEntry) }) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { moveNoteToTrashDialogShownState.value = false }) {
                            Text(text = "Dismiss")
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun SaveNoteTopAppBar(
    isEditMode: Boolean,
    onBackClick: () -> Unit,
    onSaveNoteClick: () -> Unit,
    onOpenColorPickerClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Save Note", color = MaterialTheme.colors.onPrimary) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Save note button",
                    tint = MaterialTheme.colors.onPrimary)
            }
        },
        actions = {
            // Save note icon
            IconButton(onClick = onSaveNoteClick) {
                Icon(imageVector = Icons.Filled.Check,
                    contentDescription = "Save note",
                    tint = MaterialTheme.colors.onPrimary)
            }

            // Open Color picker option
            IconButton(onClick = onOpenColorPickerClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_color_lens),
                    contentDescription = "Open color picker",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            // Delete option
            if (isEditMode) {
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete note button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )

}

@Composable
fun NoteCheckOption(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Can note be checked off?", modifier = Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onCheckedChanged)
    }
}

@Composable
fun PickedColor(color: ColorModel, onClick: () -> Unit) {
    Row(Modifier
        .padding(8.dp)
        .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Picked Color", modifier = Modifier.weight(1f))
        NoteColor(color = Color.fromHex(color.hex), size = 40.dp, border = 1.dp)
    }
}

@Composable
fun SaveNoteContent(
    note: NoteModel,
    onNoteChange: (NoteModel) -> Unit,
    onColorClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        ContentTextField(label = "Title", text = note.title, onTextChange = {
            onNoteChange.invoke(note.copy(title = it))
        })
        ContentTextField(label = "Body", text = note.content, onTextChange = {
            onNoteChange.invoke(note.copy(content = it))
        })

        val canBeCheckedOff = note.isCheckOff != null

        NoteCheckOption(isChecked = canBeCheckedOff, onCheckedChanged = {
            val isCheckedOff = if (it) false else null
            onNoteChange.invoke(note.copy(isCheckOff = isCheckedOff))
        })

        PickedColor(color = note.color, onClick = onColorClick)
    }
}

@Composable
fun ColorPicker(
    colors: List<ColorModel>,
    onColorSelect: (ColorModel) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = "Color Picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
            items(colors.size) { index ->
                val color = colors[index]
                ColorItem(
                    color = color,
                    onColorSelect = onColorSelect
                )
            }
        })
    }
}

@Composable
fun ColorItem(
    color: ColorModel,
    onColorSelect: (ColorModel) -> Unit,
) {
    Row(Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable(onClick = { onColorSelect(color) }),
        verticalAlignment = CenterVertically) {
        NoteColor(color = Color.fromHex(color.hex), size = 40.dp, border = 1.dp)
        Text(text = color.name,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SaveNoteTopAppBarPreview() {
    SaveNoteTopAppBar(false, {}, {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun ContentTextFieldPreview() {
    ContentTextField(label = "Title", text = "kelechi", onTextChange = {})
}

@Preview(showBackground = true)
@Composable
fun NoteCheckOptionPreview() {
    NoteCheckOption(true, {})
}

@Preview(showBackground = true)
@Composable
fun PickedColorPreview() {
    PickedColor(DEFAULT) {}
}

@Preview(showBackground = true)
@Composable
fun SaveNoteContentPreview() {
    SaveNoteContent(
        NoteModel(), {}, {}
    )
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    ColorPicker(listOf(ColorModel(2, "orange", "#FF9800"))) {}
}