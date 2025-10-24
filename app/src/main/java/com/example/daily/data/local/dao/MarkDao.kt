package com.example.daily.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.daily.data.local.entity.DateEntity
import com.example.daily.data.local.entity.MarkEntity
import com.example.daily.model.Mark
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkDao {
    @Query("SELECT * FROM mark_table WHERE subKey = :topic")
    fun getAllMarksByTopic(topic: String) : Flow<List<MarkEntity>>

    @Transaction
    suspend fun putMark(dateEntity: DateEntity, markEntity: MarkEntity) {
        if(isDateMarked(dateEntity.date)) {
            insertOnlyMark(markEntity)
        } else {
            insertOnlyDate(dateEntity)
            insertOnlyMark(markEntity)
        }
    }

    @Transaction
    suspend fun deleteMark(dateEntity: DateEntity, markEntity: MarkEntity) {
        deleteOnlyMark(markEntity)
        if(isMarkEmpty(markEntity.parentKey)) {
            deleteDate(dateEntity)
        }
    }

    @Query("SELECT EXISTS(SELECT 1 FROM date_table WHERE date = :date)")
    suspend fun isDateMarked(date: String) : Boolean

    @Query("SELECT NOT EXISTS(SELECT 1 FROM mark_table WHERE parentKey = :parentKey)")
    suspend fun isMarkEmpty(parentKey: String) : Boolean

    @Insert
    suspend fun insertOnlyDate(date: DateEntity)

    @Delete
    suspend fun deleteDate(date: DateEntity)

    @Insert
    suspend fun insertOnlyMark(mark: MarkEntity)

    @Delete
    suspend fun deleteOnlyMark(mark: MarkEntity)
}