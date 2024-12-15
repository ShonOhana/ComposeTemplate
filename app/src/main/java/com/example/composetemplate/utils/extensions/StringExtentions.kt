package com.example.composetemplate.utils.extensions

import kotlinx.datetime.LocalDateTime

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(this)
}

fun String.isValidPassword(): Boolean {
    return this.length > 3
}

fun String.isValidFullName(): Boolean {
    val fullNameRegex = Regex("[a-zA-Z]+( [a-zA-Z]+)+")
    return fullNameRegex.matches(this)
}

fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z])"), "$1_$2")
        .replace(" ", "_")
        .lowercase()
}

fun String.safeValueOfDate(): LocalDateTime? {
    return try {
        val newValue = this.replace("z", "").replace("Z", "")
        LocalDateTime.parse(newValue)
    } catch (e: Exception) {
        null
    }
}