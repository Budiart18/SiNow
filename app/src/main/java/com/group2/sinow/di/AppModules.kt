package com.group2.sinow.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.data.dummy.DummyNotificationDataSource
import com.group2.sinow.data.dummy.DummyNotificationDataSourceImpl
import com.group2.sinow.data.network.api.datasource.SinowApiDataSource
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.data.repository.CourseRepositoryImpl
import com.group2.sinow.data.repository.NotificationRepository
import com.group2.sinow.data.repository.NotificationRepositoryImpl
import com.group2.sinow.presentation.allpopularcourse.AllPopularCourseViewModel
import com.group2.sinow.presentation.homepage.HomeViewModel
import com.group2.sinow.presentation.notification.NotificationViewModel
import com.group2.sinow.presentation.splashscreen.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.scope.get
import org.koin.dsl.module

object AppModules {

    fun getModules(): List<Module> = listOf(
        viewModels, networkModule, dataSource, repository
    )

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { SinowApiService.invoke(get()) }
    }

    private val dataSource = module {
        single<SinowDataSource> { SinowApiDataSource(get()) }
        single<DummyNotificationDataSource> { DummyNotificationDataSourceImpl() }
    }

    private val repository = module {
        single<CourseRepository> { CourseRepositoryImpl(get()) }
        single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    }

    private val viewModels = module {
        viewModelOf(::SplashViewModel)
        viewModel { HomeViewModel(get()) }
        viewModel { AllPopularCourseViewModel(get()) }
        viewModel { NotificationViewModel(get()) }
    }

}