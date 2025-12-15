package com.sloth.proactify.data.local.datasource.task

import com.sloth.proactify.data.remote.response.DatabaseError
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskLocalDataSource {
    fun getData(id: String): Flow<Results<List<TaskModel>, DatabaseError>>
    suspend fun saveData(entity: TaskModel): Flow<Results<String, DatabaseError>>
    suspend fun updateData(entity: TaskModel): Flow<Results<String, DatabaseError>>
}