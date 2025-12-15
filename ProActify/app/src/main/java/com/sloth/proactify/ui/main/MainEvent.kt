package com.sloth.proactify.ui.main

import com.sloth.proactify.domain.model.TaskModel

sealed interface MainEvent {
    data class GetData(val id: String) : MainEvent
    data class SaveData(val data: TaskModel) : MainEvent
    data class IsEdit(val data: TaskModel, val readOnly: Boolean) : MainEvent
}