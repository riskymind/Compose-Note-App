package com.asterisk.notesapplication.domain.model

import com.asterisk.notesapplication.utils.Constants.DEFAULT
import com.asterisk.notesapplication.utils.Constants.NEW_NOTE_ID


data class NoteModel(
    val id: Long = NEW_NOTE_ID, // This value is used for new notes
    val title: String = "",
    val content: String = "",
    val isCheckOff: Boolean? = null, // null represent that the note can't be checked off
    val color: ColorModel = DEFAULT,
)
