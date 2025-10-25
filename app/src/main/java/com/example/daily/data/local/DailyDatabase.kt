package com.example.daily.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.entity.MarkEntity

@Database(
    entities = [MarkEntity::class],
    version = 2
)
abstract class DailyDatabase : RoomDatabase()  {
    abstract fun markDao() : MarkDao
}