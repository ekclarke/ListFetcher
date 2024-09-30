package com.example.listfetcher.data

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getRemoteDataList(): Flow<DataList?>

    suspend fun insert(dataList: DataList)

    suspend fun syncDataList()

    suspend fun getAllData(): Flow<List<DataObj?>>

    suspend fun getDataGroupedAndSorted(): Flow<List<DataObj?>>

    suspend fun getCleanedData(): Flow<List<DataObj?>>
}