package com.malek.giffy

import android.app.Application
import com.malek.giffy.di.appModule
import com.malek.giffy.di.dataModule
import com.malek.giffy.di.domineModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber


class GiffyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GiffyApplication)
            modules(listOf(domineModule, dataModule, appModule))
        }
    }
}