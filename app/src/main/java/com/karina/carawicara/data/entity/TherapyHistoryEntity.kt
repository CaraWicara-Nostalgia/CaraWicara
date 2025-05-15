package com.karina.carawicara.data.entity

import android.os.Build
import androidx.annotation.RequiresApi
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
    val therapyType: String,
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDate,
    val score: Int,
    val totalQuestions: Int,
    val progressPercentage: Int,
    val notes: String,
    val categoryId: String = ""
)