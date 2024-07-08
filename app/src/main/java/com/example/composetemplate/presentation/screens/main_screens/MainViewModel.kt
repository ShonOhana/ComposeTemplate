package com.example.composetemplate.presentation.screens.main_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.local.db_models.Test
import com.example.composetemplate.repositories.TestRepository
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.tag
import com.example.composetemplate.utils.type
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TestRepository) : ViewModel() {

    fun getDataFromServer() {
        viewModelScope.launch {
            repository.getData().collect {
                LogsManager().logMessage(type.VERBOSE, tag.GENERAL, it?.sources?.size.toString())
            }
        }
    }

    fun saveDataToDB() {
        viewModelScope.launch {
            repository.insertTest(Test(111, "Some Data", 2))
        }
    }

    fun loadDataFromDB() {
        viewModelScope.launch {
            repository.getTest(111).collect {
                LogsManager().logMessage(type.VERBOSE, tag.GENERAL, it.s)
            }
        }
    }

}