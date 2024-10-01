package com.example.listfetcher.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataList: List<DataObj>)

    @Transaction
    @Query("SELECT * FROM data WHERE name IS NOT null AND name != '' ORDER BY listId ASC, name ASC")
    fun getDataGroupedAndSorted(): Flow<List<DataObj>>
}