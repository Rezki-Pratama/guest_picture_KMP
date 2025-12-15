package com.sloth.proactify.di

import com.sloth.proactify.ui.main.MainViewModel
import io.mockk.mockk
import org.koin.core.module.Module
import org.koin.dsl.module

val testModule: Module = module {
    // Provide mock implementations or instances for ViewModel, repository, etc.
    single { mockk<MainViewModel>(relaxed = true) }
}