package com.asterisk.notesapplication.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ColorDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String,
)
