package com.karina.carawicara.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.karina.carawicara.DateConverter
import java.time.LocalDate

@Entity(tableName = "patients")
@TypeConverters(DateConverter::class)
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val birthDate: LocalDate,
    val address: String,
)