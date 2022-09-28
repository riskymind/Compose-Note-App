package com.asterisk.notesapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.utils.fromHex


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Note(
    modifier: Modifier = Modifier,
    note: NoteModel,
    onNoteClick: (NoteModel) -> Unit = {},
    onNoteCheckedChange: (NoteModel) -> Unit = {},
    isSelected: Boolean,
) {

    val background = if (isSelected) Color.LightGray else MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = { Text(text = note.title, maxLines = 1) },
            secondaryText = { Text(text = note.content, maxLines = 1) },
            icon = {
                /**
                 * Create a [NoteColor] Component as icon
                 */
                NoteColor(color = Color.fromHex(note.color.hex), size = 40.dp, border = 1.dp)
            },
            trailing = {
                if (note.isCheckOff != null) {
                    Checkbox(
                        checked = note.isCheckOff,
                        onCheckedChange = { isChecked ->
                            val newNote = note.copy(isCheckOff = isChecked)
                            onNoteCheckedChange.invoke(newNote)
                        },
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            },
            modifier = Modifier.clickable {
                onNoteClick.invoke(note)
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NotePreview() {
    Note(note = NoteModel(1, "Note 1", "Content 1", true), isSelected = false)
}