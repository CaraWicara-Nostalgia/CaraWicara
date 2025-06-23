package com.karina.carawicara.data

data class CardAssessment(
    val cardIndex: Int,
    val cardWord: String,
    val isCorrect: Boolean,
    val mood: String? = null,
    val quickNotes: List<String> = emptyList()
)

data class SessionAssessment(
    val cardAssessments: List<CardAssessment> = emptyList(),
    val totalCorrect: Int = 0,
    val totalCards: Int = 0
) {
    fun getMoodSummary(): Map<String, Int> {
        return cardAssessments
            .mapNotNull { it.mood }
            .groupingBy { it }
            .eachCount()
    }

    fun getQuickNotesSummary(): Map<String, Int> {
        return cardAssessments
            .flatMap { it.quickNotes }
            .groupingBy { it }
            .eachCount()
    }

    fun getDominantMood(): String {
        return getMoodSummary().maxByOrNull { it.value }?.key ?: "Tidak diketahui"
    }
}
