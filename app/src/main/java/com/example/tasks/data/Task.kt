package com.example.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "task_date") val date: String,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "minute") val minute: Int
)
