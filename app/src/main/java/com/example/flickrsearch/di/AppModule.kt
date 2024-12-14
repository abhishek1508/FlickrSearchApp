package com.example.flickrsearch.di

import com.example.flickrsearch.network.DefaultFlickrApiServiceImpl
import com.example.flickrsearch.network.FlickrApiService
import com.example.flickrsearch.repository.FlickrSearchRepository
import com.example.flickrsearch.repository.FlickrSearchRepositoryImpl
import com.example.flickrsearch.viewmodel.FlickrSearchViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Defines the Dependency Injection (DI) module.
 *
 * This module provides the following dependencies:
 * - A singleton instance of [HttpClient] created via [FlickrApiService.createClient]
 * - A singleton instance of [FlickrApiService] implemented by [DefaultFlickrApiServiceImpl], using the [HttpClient].
 * - A singleton instance of [FlickrSearchRepository] implemented by [FlickrSearchRepositoryImpl], using the [FlickrApiService].
 * - A ViewModel instance of [FlickrSearchViewModel] created using Koin's [viewModelOf].
 *
 * This setup ensures that all required dependencies are properly scoped and injected where needed.
 */
val AppModule = module {

    single<HttpClient> {
        FlickrApiService.createClient(engineFactory = Android)
    }

    single<FlickrApiService> {
        DefaultFlickrApiServiceImpl(client = get())
    }

    single<FlickrSearchRepository> {
        FlickrSearchRepositoryImpl(apiService = get())
    }

    viewModelOf(::FlickrSearchViewModel)
}