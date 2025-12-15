package com.sloth.proactify.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String = "",
    var date: String = "",
    var color: Int = 0,
    var isDone: Boolean = false,
    var description: String = "",
    var createdDate: String = ""
)