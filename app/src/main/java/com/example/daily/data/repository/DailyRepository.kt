package com.example.daily.data.repository

import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.entity.MarkEntity
import com.example.daily.model.Mark
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DailyRepository @Inject constructor(
    private val markDao: MarkDao
) {
    fun getAllMarksByTopic(topic: String) =
        markDao.getAllMarksByTopic(topic)
            .map { entityList ->
                entityList.map { markEntity ->
                    Mark(
                        markEntity.date,
                        markEntity.topic,
                        markEntity.color
                    )
                }
            }

    suspend fun putMark(mark: Mark) = markDao.putMark(
        MarkEntity(
            color = mark.color,
            date = mark.date,
            topic = mark.topic
        )
    )

    suspend fun deleteMark(mark: Mark) = markDao.deleteMark(
        MarkEntity(
            color = mark.color,
            date = mark.date,
            topic = mark.topic
        )
    )
}