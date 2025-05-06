package com.karina.carawicara.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "language_abilities",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId")]
)

data class LanguageAbilityEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val patientId: String
)