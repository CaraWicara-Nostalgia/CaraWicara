package com.karina.carawicara.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PatientWithLanguageAbilities(
    @Embedded val patient: PatientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val languageAbilities: List<LanguageAbilityEntity>,
)