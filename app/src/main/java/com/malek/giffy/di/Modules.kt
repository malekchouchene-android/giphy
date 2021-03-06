package com.malek.giffy.di

import com.malek.giffy.data.GifRepositoryImp
import com.malek.giffy.data.GiphyApi
import com.malek.giffy.domaine.GifRepository
import com.malek.giffy.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<GiphyApi> {
        Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/gifs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GiphyApi::class.java)

    }
    single<GifRepositoryImp> { GifRepositoryImp(get()) }
}

val domineModule = module {
    single<GifRepository> {
        GifRepositoryImp(get())
    }
}


val appModule = module {
    viewModel { HomeViewModel(get()) }
}