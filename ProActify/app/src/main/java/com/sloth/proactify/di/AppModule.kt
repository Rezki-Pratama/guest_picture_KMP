package com.sloth.proactify.di

fun appModule() = listOf(
    dataModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)