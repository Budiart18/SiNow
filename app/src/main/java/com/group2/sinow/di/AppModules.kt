package com.group2.sinow.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.group2.sinow.data.local.appDataStore
import com.group2.sinow.data.dummy.IntroPageDataSource
import com.group2.sinow.data.dummy.IntroPageDataSourceImpl
import com.group2.sinow.data.network.api.datasource.SinowApiDataSource
import com.group2.sinow.data.network.api.datasource.SinowDataSource
import com.group2.sinow.data.network.api.service.SinowApiService
import com.group2.sinow.data.repository.AuthRepository
import com.group2.sinow.data.repository.AuthRepositoryImpl
import com.group2.sinow.data.repository.CourseRepository
import com.group2.sinow.data.repository.CourseRepositoryImpl
import com.group2.sinow.presentation.auth.login.LoginViewModel
import com.group2.sinow.data.local.UserPreferenceDataSource
import com.group2.sinow.data.local.UserPreferenceDataSourceImpl
import com.group2.sinow.presentation.auth.otp.OTPViewModel
import com.group2.sinow.presentation.auth.register.RegisterViewModel
import com.group2.sinow.data.repository.NotificationRepository
import com.group2.sinow.data.repository.NotificationRepositoryImpl
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.data.repository.UserRepositoryImpl
import com.group2.sinow.presentation.account.AccountFeatureViewModel
import com.group2.sinow.presentation.account.settings.SettingsViewModel
import com.group2.sinow.presentation.allpopularcourse.AllPopularCourseViewModel
import com.group2.sinow.presentation.detail.DetailCourseViewModel
import com.group2.sinow.presentation.course.CourseViewModel
import com.group2.sinow.presentation.auth.forgotpassword.ForgotPasswordViewModel
import com.group2.sinow.presentation.change_password.ChangePasswordUserViewModel
import com.group2.sinow.presentation.homepage.HomeViewModel
import com.group2.sinow.presentation.notification.notificationdetail.NotificationDetailViewModel
import com.group2.sinow.presentation.notification.notificationlist.NotificationViewModel
import com.group2.sinow.presentation.transactionhistory.TransactionHistoryViewModel
import com.group2.sinow.presentation.transactionhistory.detailtransactionhistory.DetailTransactionHistoryViewModel
import com.group2.sinow.presentation.profile.ProfileViewModel
import com.group2.sinow.presentation.splash.SplashViewModel
import com.group2.sinow.presentation.userclass.UserClassViewModel
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
        single<IntroPageDataSource> { IntroPageDataSourceImpl() }
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
        viewModel { CourseViewModel(get()) }
        viewModel { UserClassViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
        viewModel { OTPViewModel(get(), get())}
        viewModel { LoginViewModel(get(), get()) }
        viewModel { AllPopularCourseViewModel(get()) }
        viewModel { NotificationViewModel(get()) }
        viewModel { ProfileViewModel(get(), get()) }
        viewModel { ChangePasswordUserViewModel(get()) }
        viewModel { params -> NotificationDetailViewModel(params.get(), get()) }
        viewModel { params -> DetailCourseViewModel(params.get(), get()) }
        viewModel {ForgotPasswordViewModel(get())}
        viewModel {TransactionHistoryViewModel(get())}
        viewModel { params -> DetailTransactionHistoryViewModel(params.get(), get()) }
        viewModel { params -> DetailCourseViewModel(params.get(), get()) }
        viewModel {ForgotPasswordViewModel(get())}
        viewModel { SplashViewModel(get(), get()) }
        viewModel { AccountFeatureViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
    }

}