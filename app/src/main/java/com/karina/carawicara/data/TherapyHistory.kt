package com.karina.carawicara.data

import java.time.LocalDate

data class TherapyHistory(
    val id: String,
    val date: LocalDate,
    val therapyType: String,
    val progressPercentage: Int,
    val notes: String,
    val showLine: Boolean = true
)
