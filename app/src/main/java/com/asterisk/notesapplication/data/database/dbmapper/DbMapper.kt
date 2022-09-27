package com.asterisk.notesapplication.data.database.dbmapper

import com.asterisk.notesapplication.data.database.model.ColorDbModel
import com.asterisk.notesapplication.data.database.model.NoteDbModel
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel

interface DbMapper {

    // NoteDbModel -> NoteModel

    fun mapNotes(
        noteDbModels: List<NoteDbModel>,
        colorDbModels: Map<Long, ColorDbModel>,
    ): List<NoteModel>

    fun mapNote(noteDbModel: NoteDbModel, colorDbModel: ColorDbModel): NoteModel

    // ColorDbMode -> ColorModel

    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel>

    fun mapColor(colorDbModel: ColorDbModel): ColorModel

    // NoteModel -> NoteDbModel

    fun mapDbNote(noteModel: NoteModel): NoteDbModel

    // ColorModel -> ColorDbModel

    fun mapDbColors(colors: List<ColorModel>): List<ColorDbModel>

    fun mapDbColor(color: ColorModel): ColorDbModel
}