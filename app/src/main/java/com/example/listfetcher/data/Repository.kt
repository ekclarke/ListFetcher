package com.example.listfetcher.data

import android.app.Activity
import com.example.listfetcher.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    val remoteData: RemoteDatasource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getList(): Flow<List<DataObj>> {
        return remoteData.getParsedList().mapNotNull { it?.list }.flowOn(ioDispatcher)
    }

}