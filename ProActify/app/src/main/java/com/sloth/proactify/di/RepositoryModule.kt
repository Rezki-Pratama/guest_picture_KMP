package com.sloth.proactify.di

import com.sloth.proactify.data.repository.TaskRepositoryImpl
import com.sloth.proactify.domain.repository.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}