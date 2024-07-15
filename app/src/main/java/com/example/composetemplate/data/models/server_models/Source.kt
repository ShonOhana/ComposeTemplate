package com.example.composetemplate.data.models.server_models

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String,
)
