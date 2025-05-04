package com.karina.carawicara.data

data class FlashcardPelafalanItem(
    val id: Int = 0,
    val imageRes: String,
    val word: String,
    val pronunciation: String,
    val category: String,
)

data class PelafalanExerciseCategory(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int = 0,
    val total: Int = 0,
    val progressPercentage: Int = 75,
)