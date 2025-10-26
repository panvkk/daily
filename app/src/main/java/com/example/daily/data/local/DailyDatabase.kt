package com.example.daily.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.dao.TopicDao
import com.example.daily.data.local.entity.MarkEntity
import com.example.daily.data.local.entity.TopicEntity

@Database(
    entities = [MarkEntity::class, TopicEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class DailyDatabase : RoomDatabase()  {
    abstract fun markDao() : MarkDao
    abstract fun topicDao() : TopicDao
}