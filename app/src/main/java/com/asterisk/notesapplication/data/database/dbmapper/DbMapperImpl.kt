package com.asterisk.notesapplication.data.database.dbmapper

import com.asterisk.notesapplication.data.database.model.ColorDbModel
import com.asterisk.notesapplication.data.database.model.NoteDbModel
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.utils.Constants.NEW_NOTE_ID

class DbMapperImpl : DbMapper {

    override fun mapNotes(
        noteDbModels: List<NoteDbModel>,
        colorDbModels: Map<Long, ColorDbModel>,
    ): List<NoteModel> = noteDbModels.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found")
        mapNote(it, colorDbModel)
    }

    override fun mapNote(noteDbModel: NoteDbModel, colorDbModel: ColorDbModel): NoteModel {
        val color = mapColor(colorDbModel)
        val isCheckedOff = with(noteDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(noteDbModel) { NoteModel(id, title, content, isCheckedOff, color) }
    }

    override fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }

    override fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }


    override fun mapDbNote(noteModel: NoteModel): NoteDbModel =
        with(noteModel) {
            val canBeCheckedOff = isCheckOff != null
            val isCheckedOff = isCheckOff ?: false

            if (id == NEW_NOTE_ID) {
                NoteDbModel(
                    title = title,
                    content = content,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    colorId = color.id,
                    isInTrash = false
                )
            } else {
                NoteDbModel(
                    id = id,
                    title = title,
                    content = content,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    colorId = color.id,
                    isInTrash = false
                )
            }
        }

    override fun mapDbColors(colors: List<ColorModel>): List<ColorDbModel> {
        return colors.map { mapDbColor(it) }
    }

    override fun mapDbColor(color: ColorModel): ColorDbModel {
        return with(color) {
            ColorDbModel(id, hex, name)
        }
    }
}