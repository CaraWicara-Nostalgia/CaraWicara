package com.karina.carawicara.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karina.carawicara.R
import com.karina.carawicara.data.FlashcardPelafalanItem
import com.karina.carawicara.data.PelafalanExerciseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PelafalanExerciseViewModel : ViewModel() {
    private val _flashcards = MutableStateFlow<List<FlashcardPelafalanItem>>(emptyList())
    val flashcards: StateFlow<List<FlashcardPelafalanItem>> = _flashcards

    private val _categories = MutableStateFlow<List<PelafalanExerciseCategory>>(emptyList())
    val categories: StateFlow<List<PelafalanExerciseCategory>> = _categories

    private val _currentCategory = MutableStateFlow<String>("")
    val currentCategory: StateFlow<String> = _currentCategory

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    init {
        loadAllFlashcards()
        loadCategories()
    }

    private fun loadAllFlashcards() {
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

    private fun loadCategories() {
        val categoryList = listOf(
            PelafalanExerciseCategory(
                id = 1,
                title = "Melafalkan konsonan 'm'",
                description = "Berlatih mengucapkan huruf 'm' dengan benar.",
                total = 5,
                progress = 0,
                progressPercentage = 80
            )
        )

        _categories.value = categoryList
    }

    // Set kategori saat ini
    fun setCurrentCategory(category: String) {
        _currentCategory.value = category
        _score.value = 0
        _isExerciseCompleted.value = false
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

    // Fungsi untuk menangani jawaban benar
    fun handleCorrectAnswer() {
        _score.value += 1

        if (currentIndex.value >= flashcards.value.size - 1) {
            _isExerciseCompleted.value = true
        } else {
            nextCard()
        }
    }

    // Fungsi untuk menangani jawaban salah
    fun handleWrongAnswer() {
        if (currentIndex.value >= flashcards.value.size - 1) {
            _isExerciseCompleted.value = true
        } else {
            nextCard()
        }
    }

    fun resetExercise() {
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
    }
}

// Factory untuk membuat instance ViewModel dengan parameter
class PelafalanExerciseViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PelafalanExerciseViewModel::class.java)) {
            return PelafalanExerciseViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}