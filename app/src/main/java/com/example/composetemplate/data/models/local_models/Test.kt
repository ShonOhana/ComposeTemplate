package com.example.composetemplate.data.models.local_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("test")
data class Test(
    @PrimaryKey
    val id: Int,
    val s: String,
    val i: Int
)