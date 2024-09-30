package com.example.listfetcher.di

import com.example.listfetcher.data.RemoteDatasource
import com.example.listfetcher.data.Repository
import dagger.Provides
import javax.inject.Singleton

class ApplicationModule {
    @Singleton
    @Provides
    fun provideRemoteDatasource(remoteDatasource: RemoteDatasource): RemoteDatasource =
        remoteDatasource

    @Singleton
    @Provides
    fun provideRepository(repository: Repository): Repository = repository
}