package com.sfeatherstone.pinformat3

import android.app.Application
import com.sfeatherstone.pinformat3.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModules)
        }

    }

    companion object {
        val koinModules = listOf(appModule)
    }

}