package com.sloth.proactify.data.local.datasource.task

import com.sloth.proactify.data.local.room.dao.TaskDao
import com.sloth.proactify.data.remote.response.DatabaseError
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.model.TaskModel
import com.sloth.proactify.utils.toDomainList
import com.sloth.proactify.utils.toRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskLocalDataSourceImpl(private val taskDao: TaskDao) : TaskLocalDataSource {
    override fun getData(id: String): Flow<Results<List<TaskModel>, DatabaseError>> = flow {
        try {
            val taskEntities = taskDao.getAllTasks()
            val domainModels = taskEntities.toDomainList()
            emit(Results.Success(domainModels))
        } catch (e: Exception) {
            emit(Results.Error(DatabaseError.UNKNOWN))
        }
    }

    override suspend fun saveData(entity: TaskModel): Flow<Results<String, DatabaseError>> = flow {
        try {
            taskDao.insert(entity.toRoom())
            emit(Results.Success("Success"))
        } catch (e: Exception) {
            emit(Results.Error(DatabaseError.UNKNOWN))
        }
    }

    override suspend fun updateData(entity: TaskModel): Flow<Results<String, DatabaseError>> = flow {
        try {
            taskDao.update(entity.toRoom())
            emit(Results.Success("Success"))
        } catch (e: Exception) {
            emit(Results.Error(DatabaseError.UNKNOWN))
        }
    }

}