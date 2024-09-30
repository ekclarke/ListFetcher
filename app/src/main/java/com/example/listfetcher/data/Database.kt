package com.example.listfetcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec


@Database(
    entities = [
        DataObj::class
    ],
    version = 1
)

abstract class Database : RoomDatabase(), AutoMigrationSpec {
    abstract fun dataDao(): DataDao
}