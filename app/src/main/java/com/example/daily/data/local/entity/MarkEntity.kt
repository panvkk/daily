package com.example.daily.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "mark_table",
    foreignKeys = [ForeignKey(
        entity = DateEntity::class,
        parentColumns = ["date"],
        childColumns = ["parentKey"],
        onDelete = CASCADE
    )]
)
data class MarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentKey: String,
    val subKey: String,
    val color: String,
    val description: String
)
