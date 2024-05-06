package com.example.composetemplate


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.database.model.Test
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.tag
import com.example.composetemplate.utils.type
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repo: TestRepository):ViewModel() {

    init {
        viewModelScope.launch {
            repo.insertTest(Test(111,"Shon king",2))

            repo.getTest(111).collect {
                LogsManager().logMessage(type.VERBOSE,tag.GENERAL, it.s)
            }
            repo.getData().collect{
                LogsManager().logMessage(type.VERBOSE,tag.GENERAL,it?.sources?.size.toString())
            }
        }
    }
}