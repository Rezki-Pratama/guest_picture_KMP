package com.sloth.proactify.di

import com.sloth.proactify.domain.usecase.TaskUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { TaskUseCase(get()) }
}