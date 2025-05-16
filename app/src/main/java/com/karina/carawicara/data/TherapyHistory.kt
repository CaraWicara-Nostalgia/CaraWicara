package com.karina.carawicara.data

import java.time.LocalDate

data class TherapyHistory(
    val id: String,
    val patientId: String,
    val therapyType: String,
    val date: LocalDate,
    val score: Int,
    val totalQuestions: Int,
    val progressPercentage: Int,
    val notes: String,
    val categoryId: String,
    val showLine: Boolean = true
)
