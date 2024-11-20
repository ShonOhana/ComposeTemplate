package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.server_models.Lecture
import com.example.composetemplate.data.remote.requests.FirebaseUserRequests
import com.example.composetemplate.managers.MainNetworkManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LecturesRepository(
    private val networkManager: MainNetworkManager,
) {

    suspend fun getLectures() =
        withContext(Dispatchers.IO) {
            val request = FirebaseUserRequests.GetLectures()
            (networkManager.sendRequest(request) as? HttpResponse)?.let {
                parseLecturesObjectToList(it)
            }
        }

    private suspend fun parseLecturesObjectToList(it: HttpResponse): List<Lecture> {
        val gson = Gson()
        val jsonObject = Gson().fromJson(it.body<String>(), JsonObject::class.java)
        /* Map the parsed JsonObject to a list of Lecture objects */
        val lectureList = jsonObject.entrySet().map { entry ->
            gson.fromJson(entry.value, Lecture::class.java)
        }
        return lectureList
    }
}