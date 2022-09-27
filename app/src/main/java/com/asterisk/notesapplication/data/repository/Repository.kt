package com.asterisk.notesapplication.data.repository

import androidx.lifecycle.LiveData
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel

/**
 *  Communication with the app's database
 */
interface Repository {

    // Notes
    fun getAllNotesNotInTrash(): LiveData<List<NoteModel>>

    fun getAllNotesInTrash(): LiveData<List<NoteModel>>

    fun getNote(id: Long): LiveData<NoteModel>

    fun insertNote(noteModel: NoteModel)

    fun deleteNote(id: Long)

    fun deleteNotes(noteIds: List<Long>)

    fun moveNoteToTrash(noteId: Long)

    fun restoreNotesFromTrash(noteIds: List<Long>)

    // Colors
    fun getAllColors(): LiveData<List<ColorModel>>

    fun getAllColorsSync(): List<ColorModel>

    fun getColor(id: Long): LiveData<ColorModel>

    fun getColorSync(id: Long): ColorModel
}