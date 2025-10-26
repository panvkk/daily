package com.example.daily.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.daily.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic_table")
    fun getAllTopics() : Flow<List<TopicEntity>>

    @Insert
    suspend fun putTopic(topicEntity: TopicEntity)

    @Delete
    suspend fun deleteTopic(topicEntity: TopicEntity)
}