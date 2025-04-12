package com.karina.carawicara.data

data class SequenceExerciseItem(
    val id: Int,
    val title: String,
    val images: List<Int>,
    val correctOrder: List<Int>,
    val category: String
)

data class SequenceExerciseCategory(
    val id: Int,
    val title: String,
    val description: String,
    val total: Int,
    val progress: Int = 0,
    val progressPercentage: Int = 0
)