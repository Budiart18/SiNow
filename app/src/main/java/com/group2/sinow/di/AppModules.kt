package com.group2.sinow.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.data.local.appDataStore
import com.group2.sinow.data.dummy.DummyNotificationDataSource
import com.group2.sinow.data.dummy.DummyNotificationDataSourceImpl
import com.group2.sinow.data.network.api.datasource.SinowApiDataSource
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.data.repository.AuthRepositoryImpl
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.data.repository.CourseRepositoryImpl
import com.group2.sinow.model.register.Register
import com.group2.sinow.presentation.auth.login.LoginViewModel
import com.group2.sinow.presentation.auth.login.UserPreferenceDataSource
import com.group2.sinow.presentation.auth.login.UserPreferenceDataSourceImpl
import com.group2.sinow.presentation.auth.otp.OTPViewModel
import com.group2.sinow.presentation.auth.register.RegisterViewModel
import com.group2.sinow.data.repository.NotificationRepository
import com.group2.sinow.data.repository.NotificationRepositoryImpl
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.data.repository.UserRepositoryImpl
import com.group2.sinow.presentation.allpopularcourse.AllPopularCourseViewModel
import com.group2.sinow.presentation.auth.changepassword.ChangePasswordViewModel
import com.group2.sinow.presentation.homepage.HomeViewModel
import com.group2.sinow.presentation.notification.notificationdetail.NotificationDetailViewModel
import com.group2.sinow.presentation.notification.notificationlist.NotificationViewModel
import com.group2.sinow.presentation.profile.ProfileViewModel
import com.group2.sinow.presentation.splashscreen.SplashViewModel
import com.group2.sinow.utils.PreferenceDataStoreHelper
import com.group2.sinow.utils.PreferenceDataStoreHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {

    fun getModules(): List<Module> = listOf(
        viewModels, networkModule, dataSource, repository
    )

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { SinowApiService.invoke(get(), get()) }
    }

    private val dataSource = module {
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
        single<SinowDataSource> { SinowApiDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }

        single<DummyNotificationDataSource> { DummyNotificationDataSourceImpl() }
    }

    private val repository = module {
        single<CourseRepository> { CourseRepositoryImpl(get()) }
        single<NotificationRepository> { NotificationRepositoryImpl(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModels = module {
        viewModelOf(::SplashViewModel)
        viewModel { HomeViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
        viewModel { OTPViewModel(get(), get())}
        viewModel { LoginViewModel(get(), get()) }
        viewModel { AllPopularCourseViewModel(get()) }
        viewModel { NotificationViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { params -> NotificationDetailViewModel(params.get(), get()) }
        viewModel {ChangePasswordViewModel(get())}
    }

}