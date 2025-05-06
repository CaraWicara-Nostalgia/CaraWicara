package com.karina.carawicara.data.entity

import androidx.room.ForeignKey
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.karina.carawicara.DateConverter
import java.time.LocalDate

@Entity(
    tableName = "therapy_histories",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("patientId")]
)
@TypeConverters(DateConverter::class)
data class TherapyHistoryEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val date: LocalDate,
    val therapyType: String,
    val progressPercentage: Int,
    val notes: String,
)