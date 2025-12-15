package com.sloth.proactify.data.repository

import com.sloth.proactify.data.local.datasource.task.TaskLocalDataSource
import com.sloth.proactify.data.remote.response.DatabaseError
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.model.TaskModel
import com.sloth.proactify.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val localDataSource: TaskLocalDataSource) : TaskRepository {

    override fun getData(id: String): Flow<Results<List<TaskModel>, DatabaseError>> {
        return localDataSource.getData(id)
    }

    override suspend fun saveData(entity: TaskModel): Flow<Results<String, DatabaseError>> {
        return localDataSource.saveData(entity)
    }

    override suspend fun updateData(entity: TaskModel): Flow<Results<String, DatabaseError>> {
        return localDataSource.updateData(entity)
    }
}