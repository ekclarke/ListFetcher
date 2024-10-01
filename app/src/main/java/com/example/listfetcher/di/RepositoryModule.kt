package com.example.listfetcher.di

import com.example.listfetcher.data.Repository
import com.example.listfetcher.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun provideRepository(repository: RepositoryImpl): Repository

    // Although this shows up in Android Studio as an unused interface and function,
    // Hilt still uses it to inject the repository into the MainViewModel
}