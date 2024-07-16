package com.example.composetemplate.repositories

import com.example.composetemplate.data.remote.requests.ExampleRequests
import com.example.composetemplate.data.remote.responses.ExampleResponse
import com.example.composetemplate.data.local.dao.TestDao
import com.example.composetemplate.data.models.local_models.Test
import com.example.composetemplate.managers.NetworkManager
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRepository(private val networkManager: NetworkManager, private val testDao: TestDao) {

    fun getData(): Flow<ExampleResponse?> {
        return flow {
            (networkManager.sendRequest(ExampleRequests.ExampleRequest()) as? HttpResponse)?.let { response ->
                emit(response.body<ExampleResponse>())
            } ?: run {
                emit(null)
            }
        }
    }

    suspend fun upsert(test: Test) = testDao.upsert(test)
    fun getItem(id: String) = testDao.getItem(id)
    fun getItems() = testDao.getItems()
    fun deleteTable() = testDao.deleteTable()

}