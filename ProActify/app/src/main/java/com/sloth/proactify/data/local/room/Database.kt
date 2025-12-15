package com.sloth.proactify.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sloth.proactify.data.local.room.dao.TaskDao

@Database(entities = [TaskEntity::class],version = 1 , exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}