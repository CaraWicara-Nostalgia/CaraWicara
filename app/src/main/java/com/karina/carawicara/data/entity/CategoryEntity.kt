package com.karina.carawicara.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val progress: Int = 0,
    val progressPercentage: Int = 0,
    val total: Int = 0,
)