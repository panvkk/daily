package com.example.daily.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_table")
data class DateEntity(
    @PrimaryKey val date: String
)
