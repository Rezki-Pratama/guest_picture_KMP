package com.sloth.proactify.domain.model

import androidx.compose.ui.graphics.Color

data class TaskModel(
    val id: Long = 0,
    val name: String,
    val date: String,
    val color: Color,
    val isDone: Boolean,
    val description: String,
    val createdDate: String
) {
    companion object {
        val empty = TaskModel(
            id = 0,
            name = "",
            date = "",
            color = Color(0),
            isDone = false,
            description = "",
            createdDate = ""
        )
    }
}