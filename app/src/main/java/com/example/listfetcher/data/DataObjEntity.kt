package com.example.listfetcher.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class DataObjEntity(
    @PrimaryKey
    val id: Int,
    val listId: Int,
    val name: String?
)

fun DataObjEntity.toDataObj() = DataObj(id = id, listId = listId, name = name)