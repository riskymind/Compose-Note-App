package com.asterisk.notesapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asterisk.notesapplication.data.database.dao.ColorDao
import com.asterisk.notesapplication.data.database.dao.NoteDao
import com.asterisk.notesapplication.data.database.dbmapper.DbMapper
import com.asterisk.notesapplication.data.database.model.ColorDbModel
import com.asterisk.notesapplication.data.database.model.NoteDbModel
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.utils.Constants.DEFAULT_COLORS
import com.asterisk.notesapplication.utils.Constants.DEFAULT_NOTES
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0

class RepositoryImpl(
    private val noteDao: NoteDao,
    private val colorDao: ColorDao,
    private val mapper: DbMapper,
) : Repository {

    init {
        initDatabase(this::updateNotesLivedata)
    }

    /**
     * Populates database with colors if it is empty.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            // Prepopulate colors
            val colors = DEFAULT_COLORS.toTypedArray()
            val dbColors = colorDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                colorDao.insertAll(*colors)
            }

            // Prepopulate notes
            val notes = DEFAULT_NOTES.toTypedArray()
            val dbNotes = noteDao.getAllSync()
            if (dbNotes.isNullOrEmpty()) {
                noteDao.insertAll(*notes)
            }

            postInitAction.invoke()
        }
    }

    private val notesNotInTrashLiveData: MutableLiveData<List<NoteModel>> by lazy {
        MutableLiveData<List<NoteModel>>()
    }

    private val notesInTrashLiveData: MutableLiveData<List<NoteModel>> by lazy {
        MutableLiveData<List<NoteModel>>()
    }

    override fun getAllNotesNotInTrash(): LiveData<List<NoteModel>> = notesNotInTrashLiveData

    override fun getAllNotesInTrash(): LiveData<List<NoteModel>> = notesInTrashLiveData

    override fun getNote(id: Long): LiveData<NoteModel> =
        Transformations.map(noteDao.findById(id)) {
            val colorDbModel = colorDao.findByIdSync(it.colorId)
            mapper.mapNote(it, colorDbModel)
        }

    override fun insertNote(noteModel: NoteModel) {
        noteDao.insert(mapper.mapDbNote(noteModel))
        updateNotesLivedata()
    }

    override fun deleteNote(id: Long) {
        noteDao.delete(id)
        updateNotesLivedata()
    }

    override fun deleteNotes(noteIds: List<Long>) {
        noteDao.delete(noteIds)
        updateNotesLivedata()
    }

    override fun moveNoteToTrash(noteId: Long) {
        val dbNote = noteDao.findByIdSync(noteId)
        val newDbNote = dbNote.copy(isInTrash = true)
        noteDao.insert(newDbNote)
        updateNotesLivedata()
    }

    override fun restoreNotesFromTrash(noteIds: List<Long>) {
        val dbNotesInTrash = noteDao.getNotesByIdsSync(noteIds)
        dbNotesInTrash.forEach {
            val newNote = it.copy(isInTrash = false)
            noteDao.insert(newNote)
        }
        updateNotesLivedata()

    }

    private fun updateNotesLivedata() {
        notesNotInTrashLiveData.postValue(getAllNotesDependingOnTrashStateSync(false))
        val newNotesInTrashLivedata = getAllNotesDependingOnTrashStateSync(true)
        notesInTrashLiveData.postValue(newNotesInTrashLivedata)
    }

    private fun getAllNotesDependingOnTrashStateSync(inTrash: Boolean): List<NoteModel> {
        val colorDbModels: Map<Long, ColorDbModel> =
            colorDao.getAllSync().map { it.id to it }.toMap()
        val dbNotesNotInTrash: List<NoteDbModel> =
            noteDao.getAllSync().filter { it.isInTrash == inTrash }
        return mapper.mapNotes(dbNotesNotInTrash, colorDbModels)
    }

    override fun getAllColors(): LiveData<List<ColorModel>> =
        Transformations.map(colorDao.getAll()) { mapper.mapColors(it) }

    override fun getAllColorsSync(): List<ColorModel> =
        mapper.mapColors(colorDao.getAllSync())

    override fun getColor(id: Long): LiveData<ColorModel> =
        Transformations.map(colorDao.findById(id)) { mapper.mapColor(it) }

    override fun getColorSync(id: Long): ColorModel =
        mapper.mapColor(colorDao.findByIdSync(id))
}