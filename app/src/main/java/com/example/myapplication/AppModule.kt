package com.example.myapplication

import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { OkHttpClient() }
    single { NetworkManager(get()) }
    single<StudentApi> { StudentApiImpl(get()) }
    viewModel<StudentViewModel> { StudentViewModelImpl(get()) }
}
