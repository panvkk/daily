package com.example.daily.data.repository

import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.entity.DateEntity
import com.example.daily.data.local.entity.MarkEntity
import com.example.daily.model.Date
import com.example.daily.model.Mark
import kotlinx.coroutines.flow.map

class DailyRepository(private val markDao: MarkDao) {
    fun getAllMarksByTopic(topic: String) =
        markDao.getAllMarksByTopic(topic)
            .map { entityList ->
                entityList.map { markEntity ->
                    Mark(
                        markEntity.subKey,
                        markEntity.color,
                        markEntity.description
                    )
                }
            }

    suspend fun putMark(date: Date, mark: Mark) = markDao.putMark(
        DateEntity(date.date),
        MarkEntity(
            parentKey = date.date,
            subKey = mark.topic,
            color = mark.color,
            description = mark.description
        )
    )

    suspend fun deleteMark(date: Date, mark: Mark) = markDao.deleteMark(
        DateEntity(date.date),
        MarkEntity(
            parentKey = date.date,
            subKey = mark.topic,
            color = mark.color,
            description = mark.description
        )
    )
}