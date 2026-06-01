package com.example.myapplication

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.RoomManager
import com.example.myapplication.database.RoomManagerImpl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { OkHttpClient() }
    single { NetworkManager(get()) }
    single<StudentApi> { StudentApiImpl(get()) }
    viewModel<StudentViewModel> { StudentViewModelImpl(get()) }

    single { AppDatabase.create(androidContext()) }
    single<RoomManager> { RoomManagerImpl(get()) }
}
