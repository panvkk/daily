package com.example.daily.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_table")
data class TopicEntity(
    @PrimaryKey val name: String,
    val specs: List<TopicSpecDto>
)
