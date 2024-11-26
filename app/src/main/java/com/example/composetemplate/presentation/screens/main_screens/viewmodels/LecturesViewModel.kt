package com.example.composetemplate.presentation.screens.main_screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.base.BaseViewModel
import com.example.composetemplate.data.models.server_models.Lecture
import com.example.composetemplate.repositories.LecturesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LecturesViewModel(
    private val lecturesRepository: LecturesRepository
) : BaseViewModel() {

    private val _lectures = MutableStateFlow<List<Lecture>>(emptyList())
    val lectures: StateFlow<List<Lecture>> = _lectures

    private suspend fun fetchLectures() {
        val fetchedLectures = lecturesRepository.getLectures() ?: emptyList()
        _lectures.value = fetchedLectures
    }

    init {
        viewModelScope.launch {
            fetchLectures()
        }
    }

}