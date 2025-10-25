package com.example.daily.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.daily.data.local.entity.MarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkDao {
    @Query("SELECT * FROM mark_table WHERE topic = :topic")
    fun getAllMarksByTopic(topic: String) : Flow<List<MarkEntity>>

    @Transaction
    suspend fun deleteMark(markNoId: MarkEntity) {
        val id = getByInfo(color = markNoId.color, topic = markNoId.topic, date = markNoId.date).id
        deleteMarkById(id)
    }

    @Query("SELECT * FROM mark_table WHERE " +
            "color = :color AND " +
            "topic = :topic AND " +
            "date = :date")
    suspend fun getByInfo(color: Long, topic: String, date: String) : MarkEntity

    @Insert
    suspend fun putMark(mark: MarkEntity)

    @Query("DELETE FROM mark_table WHERE id = :id ")
    suspend fun deleteMarkById(id: Int)
}