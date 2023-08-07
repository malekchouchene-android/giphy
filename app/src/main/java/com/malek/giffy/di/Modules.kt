package com.malek.giffy.di

import com.malek.giffy.BuildConfig
import com.malek.giffy.data.GifRepositoryImp
import com.malek.giffy.data.GiphyApi
import com.malek.giffy.domaine.GIFRepository
import com.malek.giffy.ui.search.SearchViewModel
import com.malek.giffy.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val dataModule = module {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .readTimeout(7, TimeUnit.SECONDS)
        .connectTimeout(7, TimeUnit.SECONDS)
        .build()
    single<GiphyApi> {
        Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/gifs/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GiphyApi::class.java)

    }
    single<GifRepositoryImp> { GifRepositoryImp(get()) }
}

val domineModule = module {
    single<GIFRepository> {
        GifRepositoryImp(get())
    }
}


val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}