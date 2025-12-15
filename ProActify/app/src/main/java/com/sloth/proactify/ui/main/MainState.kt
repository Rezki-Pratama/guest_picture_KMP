package com.sloth.proactify.ui.main

import com.sloth.proactify.domain.model.TaskModel

data class MainState(
    val data: List<TaskModel> = listOf(),
    val message: String = "",
    val editData: TaskModel? = TaskModel.empty,
    val isEdit: Boolean = false,
    val isReadOnly: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean = false,
)