package com.example.daily.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.entity.DateEntity
import com.example.daily.data.local.entity.MarkEntity

@Database(
    entities = [DateEntity::class, MarkEntity::class],
    version = 1
)
abstract class DailyDatabase : RoomDatabase()  {
    abstract fun markDao() : MarkDao
}