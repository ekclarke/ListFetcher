package com.example.listfetcher.data

import com.example.listfetcher.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val remoteData: RemoteDatasource,
    private val dataDao: DataDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): Repository {
    override suspend fun getRemoteDataList(): Flow<DataList?> =
        withContext(ioDispatcher) { remoteData.getParsedList() }

    override suspend fun insert(dataList: DataList): Unit = withContext(ioDispatcher) {
        dataDao.insert(dataList.list)
    }

    override suspend fun syncDataList(): Unit = withContext(ioDispatcher)
    {
        getRemoteDataList().map { dataList ->
            if (dataList != null)
                insert(dataList)
        }

    }

    override suspend fun getAllData(): Flow<List<DataObj?>> = dataDao.getAllData().flowOn(
        ioDispatcher
    )

    override suspend fun getDataGroupedAndSorted(): Flow<List<DataObj?>> =
        dataDao.getDataGroupedAndSorted().flowOn(
            ioDispatcher
        )

    override suspend fun getCleanedData(): Flow<List<DataObj?>> =
        getDataGroupedAndSorted().map { data -> data.filter { it?.name.isNullOrBlank() } }
            .flowOn(ioDispatcher)
}