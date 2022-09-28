package com.asterisk.notesapplication.utils

import com.asterisk.notesapplication.data.database.model.ColorDbModel
import com.asterisk.notesapplication.data.database.model.NoteDbModel
import com.asterisk.notesapplication.domain.model.ColorModel

object Constants {

    const val DATABASE_NAME = "note_database"

    val DEFAULT_NOTES = listOf(
        NoteDbModel(1, "RW Meeting", "Prepare sample project", false, false, 1, false),
        NoteDbModel(2, "Bills", "Pay by tomorrow", false, false, 2, false),
        NoteDbModel(3, "Pancake recipe", "Milk, eggs, salt, flour...", false, false, 3, false),
        NoteDbModel(4, "Workout", "Running, push ups, pull ups, squats...", false, false, 4, false),
        NoteDbModel(5, "Title 5", "Content 5", false, false, 5, false),
        NoteDbModel(6, "Title 6", "Content 6", false, false, 6, false),
        NoteDbModel(7, "Title 7", "Content 7", false, false, 7, false),
        NoteDbModel(8, "Title 8", "Content 8", false, false, 8, false),
        NoteDbModel(9, "Title 9", "Content 9", false, false, 9, false),
        NoteDbModel(10, "Title 10", "Content 10", false, false, 10, false),
        NoteDbModel(11, "Title 11", "Content 11", true, false, 11, false),
        NoteDbModel(12, "Title 12", "Content 12", true, false, 12, false)
    )

    val DEFAULT_COLORS = listOf(
        ColorDbModel(1, "#FFFFFF", "White"),
        ColorDbModel(2, "#E57373", "Red"),
        ColorDbModel(3, "#F06292", "Pink"),
        ColorDbModel(4, "#CE93D8", "Purple"),
        ColorDbModel(5, "#2196F3", "Blue"),
        ColorDbModel(6, "#00ACC1", "Cyan"),
        ColorDbModel(7, "#26A69A", "Teal"),
        ColorDbModel(8, "#4CAF50", "Green"),
        ColorDbModel(9, "#8BC34A", "Light Green"),
        ColorDbModel(10, "#CDDC39", "Lime"),
        ColorDbModel(11, "#FFEB3B", "Yellow"),
        ColorDbModel(12, "#FF9800", "Orange"),
        ColorDbModel(13, "#BCAAA4", "Brown"),
        ColorDbModel(14, "#9E9E9E", "Gray")
    )

    val DEFAULT_COLOR = DEFAULT_COLORS[0]

    val DEFAULT = with(DEFAULT_COLOR) { ColorModel(id, name, hex) }

    const val NEW_NOTE_ID = -1L

    const val NO_DIALOG = 1
    const val RESTORE_NOTES_DIALOG = 2
    const val PERMANENTLY_DELETE_DIALOG = 3
}