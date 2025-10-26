package com.example.daily.data.repository

import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.dao.TopicDao
import com.example.daily.data.local.entity.MarkEntity
import com.example.daily.data.local.entity.TopicEntity
import com.example.daily.data.local.entity.TopicSpecDto
import com.example.daily.model.Mark
import com.example.daily.model.Topic
import com.example.daily.model.TopicSpec
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DailyRepository @Inject constructor(
    private val markDao: MarkDao,
    private val topicDao: TopicDao
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

    fun getAllTopics() =
        topicDao.getAllTopics()
            .map { topicEntities ->
                topicEntities.map { topicEntity ->
                    Topic(
                        name = topicEntity.name,
                        specs = topicEntity.specs.map { specDto ->
                            TopicSpec(
                                specDto.color,
                                specDto.description
                            )
                        }
                    )
                }
            }

    suspend fun putTopic(topic: Topic) =
        topicDao.putTopic(
            TopicEntity(
                name = topic.name,
                specs = topic.specs.map { spec ->
                    TopicSpecDto(
                        color = spec.color,
                        description = spec.description
                    )
                }
            )
        )

    suspend fun deleteTopic(topic: Topic) =
        topicDao.deleteTopic(
            TopicEntity(
                name = topic.name,
                specs = topic.specs.map { spec ->
                    TopicSpecDto(
                        color = spec.color,
                        description = spec.description
                    )
                }
            )
        )
}