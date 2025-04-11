package com.karina.carawicara.data

data class FlashcardKosakataItem(
    val id: Int,
    val imageRes: Int,
    val word: String,
    val pronunciation: String,
    val category: String,
)

data class KosakataExerciseCategory(
    val id: Int,
    val title: String,
    val description: String,
    val progress: Int = 0,
    val total: Int = 0,
    val progressPercentage: Int = 75,
)