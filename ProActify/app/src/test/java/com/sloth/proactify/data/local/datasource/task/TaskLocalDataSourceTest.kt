package com.sloth.proactify.data.local.datasource.task

import com.sloth.proactify.data.local.room.TaskEntity
import com.sloth.proactify.data.local.room.dao.TaskDao
import com.sloth.proactify.data.remote.response.DatabaseError
import com.sloth.proactify.data.remote.response.Results
import com.sloth.proactify.domain.model.TaskModel
import org.junit.Before
import com.sloth.proactify.utils.toDomainList
import com.sloth.proactify.utils.toRoom
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class TaskLocalDataSourceTest: KoinTest {

    private val taskDao: TaskDao = mockk(relaxed = true)
    private val taskLocalDataSource: TaskLocalDataSource by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single { taskDao }
                    single<TaskLocalDataSource> { TaskLocalDataSourceImpl(get()) }
                }
            )
        }
    }

    @Test
    fun `getData should return Success with TaskModel list when taskDao returns data`() = runTest {
        // Arrange
        val taskEntities = listOf(mockk<TaskEntity>(relaxed = true))
        val taskModels = taskEntities.toDomainList() // Assuming toDomainList() is defined correctly
        coEvery { taskDao.getAllTasks() } returns taskEntities

        // Act
        val result = taskLocalDataSource.getData("")

        // Assert
        result.collect { outcome ->
            assertEquals(outcome, Results.Success(taskModels))
        }

        coVerify { taskDao.getAllTasks() }
    }

    @Test
    fun `getData should return Error when exception occurs`() = runTest {
        // Arrange
        coEvery { taskDao.getAllTasks() } throws Exception("Database error")

        // Act
        val result = taskLocalDataSource.getData("1")

        // Assert
        result.collect { outcome ->
            assertEquals(outcome, Results.Error(DatabaseError.UNKNOWN))
        }
        coVerify { taskDao.getAllTasks() }
    }

    @Test
    fun `saveData should return Success when taskDao insert is successful`() = runTest {
        // Arrange
        val taskModel = mockk<TaskModel>(relaxed = true)

        coEvery { taskDao.insert(any()) } returns Unit

        // Act
        val result = taskLocalDataSource.saveData(taskModel)

        // Assert
        result.collect { outcome ->
            assertEquals(outcome, Results.Success("Success"))
        }
        coVerify { taskDao.insert(taskModel.toRoom()) }
    }

    @Test
    fun `saveData should return Error when exception occurs`() = runTest {
        // Arrange
        val taskModel = mockk<TaskModel>(relaxed = true)
        coEvery { taskDao.insert(any()) } throws Exception("Insert failed")

        // Act
        val result = taskLocalDataSource.saveData(taskModel)

        result.collect { outcome ->
            assertEquals(outcome, Results.Error(DatabaseError.UNKNOWN))
        }
        coVerify { taskDao.insert(taskModel.toRoom()) }
    }

    @Test
    fun `updateData should return Success when taskDao update is successful`() = runTest {
        // Arrange
        val taskModel = mockk<TaskModel>(relaxed = true)
        coEvery { taskDao.update(any()) } returns Unit

        // Act
        val result = taskLocalDataSource.updateData(taskModel)

        // Assert
        result.collect { outcome ->
            assertEquals(outcome, Results.Success("Success"))
        }
        coVerify { taskDao.update(taskModel.toRoom()) }
    }

    @Test
    fun `updateData should return Error when exception occurs`() = runTest {
        // Arrange
        val taskModel = mockk<TaskModel>(relaxed = true)
        coEvery { taskDao.update(any()) } throws Exception("Update failed")

        // Act
        val result = taskLocalDataSource.updateData(taskModel)

        // Assert
        result.collect { outcome ->
            assertEquals(outcome, Results.Error(DatabaseError.UNKNOWN))
        }
        coVerify { taskDao.update(taskModel.toRoom()) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}