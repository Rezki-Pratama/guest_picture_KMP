package com.sloth.proactify.di

import com.sloth.proactify.ui.main.MainViewModel
import com.sloth.proactify.ui.main.TimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        TimerViewModel()
    }
}