package com.karina.carawicara.data

data class FlashcardKosakataItem(
    val id: Int = 0,
    val imageRes: String,
    val word: String,
    val pronunciation: String,
    val category: String,
)