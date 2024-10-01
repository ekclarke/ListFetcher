package com.example.listfetcher.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class DataObj(
    @PrimaryKey
    val id: Int,
    val listId: Int,
    val name: String?
)

// This could be broken down into multiple data types for more complex handling
// For example, DataObjEntity, DataObjNetwork, DataObjModel
// But in this case, these would all be identical, so I've simplified
