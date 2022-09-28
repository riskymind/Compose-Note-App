package com.asterisk.notesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.notesapplication.data.repository.Repository
import com.asterisk.notesapplication.domain.model.ColorModel
import com.asterisk.notesapplication.domain.model.NoteModel
import com.asterisk.notesapplication.routing.NotesRouter
import com.asterisk.notesapplication.routing.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: Repository) : ViewModel() {

    val notesNotInTrash: LiveData<List<NoteModel>> by lazy {
        repository.getAllNotesNotInTrash()
    }

    val notesInTrash: LiveData<List<NoteModel>> by lazy { repository.getAllNotesInTrash() }

    private var _noteEntry = MutableLiveData(NoteModel())
    val noteEntry: LiveData<NoteModel> = _noteEntry

    private var _selectedNotes = MutableLiveData<List<NoteModel>>(listOf())
    val selectedNotes: LiveData<List<NoteModel>> = _selectedNotes

    val colors: LiveData<List<ColorModel>> by lazy {
        repository.getAllColors()
    }

    fun onCreateNote() {
        _noteEntry.value = NoteModel()
        NotesRouter.navigateTo(Screen.SaveNote)
    }

    fun onNoteEntryChange(note: NoteModel) {
        _noteEntry.value = note
    }

    fun saveNote(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
            withContext(Dispatchers.Main) {
                NotesRouter.navigateTo(Screen.Notes)
                _noteEntry.value = NoteModel()
            }
        }
    }

    fun onNoteCheckedChange(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
        }
    }

    fun onNoteClick(note: NoteModel) {
        _noteEntry.value = note
        NotesRouter.navigateTo(Screen.SaveNote)
    }

    fun moveNoteToTrash(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveNoteToTrash(note.id)
            withContext(Dispatchers.Main) {
                NotesRouter.navigateTo(Screen.Notes)
            }
        }
    }

    fun onNoteSelected(note: NoteModel) {
        _selectedNotes.value = _selectedNotes.value!!.toMutableList().apply {
            if (contains(note)) remove(note) else add(note)
        }
    }

    fun restoreNotes(selectedNotes: List<NoteModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreNotesFromTrash(selectedNotes.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedNotes.value = listOf()
            }
        }
    }

    fun deleteNotesPermanently(selectedNotes: List<NoteModel>) {
        viewModelScope.launch {
            repository.deleteNotes(selectedNotes.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedNotes.value = listOf()
            }
        }
    }
}
