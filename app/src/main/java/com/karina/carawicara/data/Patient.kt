package com.karina.carawicara.data

import java.time.LocalDate

data class Patient (
    val id: String,
    val name: String,
    val birthDate: LocalDate,
    val age: Int,
    val address: String,
    val languageAbilities: List<LanguageAbility> = emptyList()
)

data class LanguageAbility(
    val id: String,
    val description: String,
    val isSelected: Boolean = false
)

data class TherapyHistory(
    val id: String,
    val patientId: String,
    val date: LocalDate,
    val therapyType: String,
    val progressPercentage: Int,
    val notes: String,
    val showLine: Boolean = true
)