package com.sloth.proactify.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.sloth.proactify.data.local.room.TaskEntity
import com.sloth.proactify.domain.model.TaskModel

fun TaskEntity.toDomain(): TaskModel {
    return TaskModel(
        id = this.id,
        name = this.name,
        date = this.date,
        color = toColor(this.color),
        isDone = this.isDone,
        description = this.description,
        createdDate = this.createdDate
    )
}

fun TaskModel.toRoom(): TaskEntity {
    return TaskEntity().apply {
        id = this@toRoom.id
        name = this@toRoom.name
        date = this@toRoom.date
        color = fromColor(this@toRoom.color)
        isDone = this@toRoom.isDone
        description = this@toRoom.description
        createdDate = this@toRoom.createdDate
    }
}

fun List<TaskEntity>.toDomainList(): List<TaskModel> {
    return this.map { it.toDomain() }
}

fun List<TaskModel>.toRoomList(): List<TaskEntity> {
    return this.map { it.toRoom() }
}

fun fromColor(color: Color) : Int {
    return color.toArgb()
}

fun toColor(color:Int) : Color {
    return Color(color)
}