package com.example.listfetcher.data

import com.example.listfetcher.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val remoteData: RemoteDatasource,
    private val dataDao: DataDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : Repository {
    override suspend fun syncDataList(): Unit = withContext(ioDispatcher)
    {
        remoteData.getParsedList().collect { dataList ->
            if (dataList != null)
                dataDao.insert(dataList)
            else
                dataDao.insert(emptyList())
        }
    }

    // Alternative options include sanitizing data before insert
    // or using mapping and filtering
    // but this is least memory-intensive approach
    // that still preserves all original data
    override suspend fun getCleanedData(): Flow<List<DataObj>> =
        dataDao.getDataGroupedAndSorted().flowOn(
            ioDispatcher
        )
}