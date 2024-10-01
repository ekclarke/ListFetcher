package com.example.listfetcher.di

import android.content.Context
import androidx.room.Room
import com.example.listfetcher.data.DataDao
import com.example.listfetcher.data.Database
import com.example.listfetcher.data.RemoteDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun provideRemoteDatasource(@IoDispatcher dispatcher: CoroutineDispatcher): RemoteDatasource {
        return RemoteDatasource(dispatcher)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room
            .databaseBuilder(
                context,
                Database::class.java,
                "database"
            ) // You can future-proof your database by making its name a constant
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(database: Database): DataDao = database.dataDao()
}