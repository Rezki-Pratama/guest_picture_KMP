package com.sloth.proactify.domain.usecase

import com.sloth.proactify.data.remote.response.DatabaseError
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.model.TaskModel
import com.sloth.proactify.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskUseCase(private val repository: TaskRepository) {
    operator fun get(id: String): Flow<Results<List<TaskModel>, DatabaseError>> {
        return repository.getData(id)
    }

    suspend fun save(entity: TaskModel): Flow<Results<String, DatabaseError>> {
        return repository.saveData(entity)
    }

    suspend fun update(entity: TaskModel): Flow<Results<String, DatabaseError>> {
        return repository.updateData(entity)
    }
}