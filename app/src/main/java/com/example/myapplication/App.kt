package com.example.myapplication

import android.app.Application
import com.example.myapplication.database.RoomManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val roomManager: RoomManager by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        appScope.launch {
            roomManager.queuedRequests().create(
                url = "https://api.example.com/test",
                method = "GET"
            )
        }
    }
}
