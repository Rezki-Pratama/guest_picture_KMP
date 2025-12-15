package com.sloth.guess_picture_app.utils

internal expect class SharedFileReader() {
    fun loadJsonFile(fileName: String): String?
}