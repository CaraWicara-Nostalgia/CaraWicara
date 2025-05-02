package com.karina.carawicara.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "kosakata",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)

data class KosakataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val pronunciation: String,
    val imageRes: String,
    val categoryId: String,
)