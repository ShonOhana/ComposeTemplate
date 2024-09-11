package com.example.composetemplate.utils.extensions

import io.ktor.client.statement.HttpResponse

fun HttpResponse.isSuccessful() = status.value in 200 .. 299