package com.sloth.proactify.domain.model

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test

class TaskModelTest {

    @Test
    fun `should create TaskModel with valid properties`() {
        val task = TaskModel(
            id = 1,
            name = "Test Task",
            date = "2023-12-31",
            color = Color(123),
            isDone = false,
            description = "This is a test task description.",
            createdDate = "2023-12-01"
        )

        assertEquals(1, task.id)
        assertEquals("Test Task", task.name)
        assertEquals("2023-12-31", task.date)
        assertEquals(Color(123), task.color)
        assertFalse(task.isDone)
        assertEquals("This is a test task description.", task.description)
        assertEquals("2023-12-01", task.createdDate)
    }

    @Test
    fun `should create empty TaskModel`() {
        val emptyTask = TaskModel.empty

        assertEquals(0, emptyTask.id)
        assertEquals("", emptyTask.name)
        assertEquals("", emptyTask.date)
        assertEquals(Color(0), emptyTask.color)
        assertFalse(emptyTask.isDone)
        assertEquals("", emptyTask.description)
        assertEquals("", emptyTask.createdDate)
    }
}