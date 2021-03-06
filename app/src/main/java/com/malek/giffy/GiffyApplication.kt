package com.malek.giffy

import android.app.Application
import com.malek.giffy.di.appModule
import com.malek.giffy.di.dataModule
import com.malek.giffy.di.domineModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class GiffyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@GiffyApplication)
            androidLogger()
            modules(*arrayOf(domineModule, dataModule, appModule))
        }
    }
}