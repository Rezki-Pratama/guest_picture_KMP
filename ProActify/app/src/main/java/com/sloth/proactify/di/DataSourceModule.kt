package com.sloth.proactify.di

import com.sloth.proactify.data.local.datasource.task.TaskLocalDataSource
import com.sloth.proactify.data.local.datasource.task.TaskLocalDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<TaskLocalDataSource> { TaskLocalDataSourceImpl(get()) }
}