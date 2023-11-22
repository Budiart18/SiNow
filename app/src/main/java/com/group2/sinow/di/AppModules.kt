package com.group2.sinow.di

import com.group2.sinow.presentation.splashscreen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {

    fun getModules(): List<Module> = listOf(
        viewModels
    )

    private val viewModels = module {
        viewModelOf(::SplashViewModel)
    }

}