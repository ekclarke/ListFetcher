package com.example.listfetcher.data

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun syncDataList()
    suspend fun getCleanedData(): Flow<List<DataObj>>
}