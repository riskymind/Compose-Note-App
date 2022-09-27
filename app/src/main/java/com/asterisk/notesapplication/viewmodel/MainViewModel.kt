package com.asterisk.notesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.asterisk.notesapplication.data.repository.Repository
import com.asterisk.notesapplication.domain.model.NoteModel

class MainViewModel(private val repository: Repository) : ViewModel() {

    val notesNotInTrash: LiveData<List<NoteModel>> by lazy {
        repository.getAllNotesNotInTrash()
    }
}
