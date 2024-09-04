package com.example.composetemplate.presentation.screens.main_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.models.local_models.Test
import com.example.composetemplate.managers.DataStoreManager
import com.example.composetemplate.repositories.TestRepository
import com.example.composetemplate.utils.Constants.Companion.DS_TEST_KEY
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.enums.DataStoreType
import com.example.composetemplate.utils.tag
import com.example.composetemplate.utils.type
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: TestRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun getDataFromServer() {
        viewModelScope.launch {
            repository.getData().collect {
                LogsManager().logMessage(type.VERBOSE, tag.GENERAL, it?.sources?.size.toString())
            }
        }
    }

    fun saveDataToDB() {
        viewModelScope.launch {
            repository.upsert(Test(111, "Some Data", 2))
        }
    }

    fun writeDataToDS() {
        viewModelScope.launch {
            dataStoreManager.readDSValue(DS_TEST_KEY, DataStoreType.STRING).collect { data ->
                (data as? String)?.let {
                    LogsManager().logMessage(type.VERBOSE, tag.DATA_STORE, "read data $it from DS")
                } ?: run {
                    val dataToBeSaved = "Some Data"
                    LogsManager().logMessage(type.VERBOSE, tag.DATA_STORE, "save value to DS")
                    dataStoreManager.writeDSValue(DS_TEST_KEY, DataStoreType.STRING, dataToBeSaved)
                }
            }
        }
    }

    fun loadDataFromDB() {
        viewModelScope.launch {
            repository.getItem("111").collect {
                LogsManager().logMessage(type.VERBOSE, tag.GENERAL, it.s)
            }
        }
    }
}