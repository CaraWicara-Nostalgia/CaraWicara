package com.karina.carawicara.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karina.carawicara.R
import com.karina.carawicara.data.FlashcardPelafalanItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PelafalanExerciseViewModel : ViewModel() {
    private val _flashcards = MutableStateFlow<List<FlashcardPelafalanItem>>(emptyList())
    val flashcards: StateFlow<List<FlashcardPelafalanItem>> = _flashcards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        // Data contoh, dalam aplikasi nyata ini akan diambil dari repository/database
        val dummyData = listOf(
            FlashcardPelafalanItem(1, R.drawable.child_boy, "HIDUNG", "/-ng-/"),
            FlashcardPelafalanItem(2, R.drawable.child_boy, "MULUT", "/-t/"),
            FlashcardPelafalanItem(3, R.drawable.child_boy, "MATA", "/-ta/"),
            FlashcardPelafalanItem(4, R.drawable.child_boy, "TELINGA", "/-nga/"),
            FlashcardPelafalanItem(5, R.drawable.child_boy, "KEPALA", "/-la/")
        )
        _flashcards.value = dummyData
    }

    // Fungsi untuk mengganti flashcard saat ini
    fun setCurrentIndex(index: Int) {
        if (index >= 0 && index < flashcards.value.size) {
            _currentIndex.value = index
        }
    }

    fun shuffleCards() {
        _flashcards.value = flashcards.value.shuffled()
        _currentIndex.value = 0
    }

    // Fungsi untuk pindah ke flashcard berikutnya
    fun nextCard() {
        if (currentIndex.value < flashcards.value.size - 1) {
            _currentIndex.value += 1
        }
    }

    fun previousCard() {
        if (currentIndex.value > 0) {
            _currentIndex.value -= 1
        }
    }

    // Fungsi untuk menangani jawaban benar
    fun handleCorrectAnswer() {
        // Logika untuk jawaban benar (misalnya: menandai kartu sebagai benar, update skor, dll)
        // Kemudian lanjut ke kartu berikutnya
        nextCard()
    }

    // Fungsi untuk menangani jawaban salah
    fun handleWrongAnswer() {
        // Logika untuk jawaban salah
        // Kemudian lanjut ke kartu berikutnya
        nextCard()
    }
}

// Factory untuk membuat instance ViewModel dengan parameter
class FlashcardViewModelFactory(private val initialIndex: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PelafalanExerciseViewModel::class.java)) {
            val viewModel = PelafalanExerciseViewModel()
            viewModel.setCurrentIndex(initialIndex)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}