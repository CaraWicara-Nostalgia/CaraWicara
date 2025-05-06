package com.karina.carawicara.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PatientWithTherapyHistories(
    @Embedded val patient: PatientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId",
    )
    val therapyHistoryEntity: TherapyHistoryEntity
)
