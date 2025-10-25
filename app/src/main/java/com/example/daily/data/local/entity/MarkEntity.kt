package com.example.daily.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mark_table",)
data class MarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val topic: String,
    val color: Long,
)
