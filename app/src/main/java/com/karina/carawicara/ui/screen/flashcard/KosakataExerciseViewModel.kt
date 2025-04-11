package com.karina.carawicara.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karina.carawicara.R
import com.karina.carawicara.data.FlashcardKosakataItem
import com.karina.carawicara.data.KosakataExerciseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KosakataExerciseViewModel : ViewModel() {
    // Daftar semua flashcard di semua kategori
    private val _allFlashcards = MutableStateFlow<Map<String, List<FlashcardKosakataItem>>>(emptyMap())
    val allFlashcards: StateFlow<Map<String, List<FlashcardKosakataItem>>> = _allFlashcards

    // Daftar kategori untuk tampilan utama
    private val _categories = MutableStateFlow<List<KosakataExerciseCategory>>(emptyList())
    val categories: StateFlow<List<KosakataExerciseCategory>> = _categories

    // Kategori yang sedang aktif
    private val _currentCategory = MutableStateFlow<String>("")
    val currentCategory: StateFlow<String> = _currentCategory

    // Flashcard yang sedang aktif dalam kategori
    private val _currentFlashcards = MutableStateFlow<List<FlashcardKosakataItem>>(emptyList())
    val currentFlashcards: StateFlow<List<FlashcardKosakataItem>> = _currentFlashcards

    // Index flashcard saat ini dalam kategori aktif
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    // Skor saat ini untuk kategori aktif
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    // Status penyelesaian latihan
    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    init {
        loadAllFlashcards()
        loadCategories()
    }

    private fun loadAllFlashcards() {
        // Data contoh, dalam aplikasi nyata ini akan diambil dari repository/database
        val buahData = listOf(
            FlashcardKosakataItem(1, R.drawable.child_boy, "APEL", "/a-pel/", "buah"),
            FlashcardKosakataItem(2, R.drawable.child_boy, "PISANG", "/pi-sang/", "buah"),
            FlashcardKosakataItem(3, R.drawable.child_boy, "MANGGA", "/mang-ga/", "buah"),
            FlashcardKosakataItem(4, R.drawable.child_boy, "JERUK", "/je-ruk/", "buah"),
            FlashcardKosakataItem(5, R.drawable.child_boy, "ANGGUR", "/ang-gur/", "buah")
        )

        val hewanData = listOf(
            FlashcardKosakataItem(6, R.drawable.child_boy, "KUCING", "/ku-cing/", "hewan"),
            FlashcardKosakataItem(7, R.drawable.child_boy, "ANJING", "/an-jing/", "hewan"),
            FlashcardKosakataItem(8, R.drawable.child_boy, "SAPI", "/sa-pi/", "hewan"),
            FlashcardKosakataItem(9, R.drawable.child_boy, "AYAM", "/a-yam/", "hewan"),
            FlashcardKosakataItem(10, R.drawable.child_boy, "KUDA", "/ku-da/", "hewan")
        )

        val pakaianData = listOf(
            FlashcardKosakataItem(11, R.drawable.child_boy, "BAJU", "/ba-ju/", "pakaian"),
            FlashcardKosakataItem(12, R.drawable.child_boy, "CELANA", "/ce-la-na/", "pakaian"),
            FlashcardKosakataItem(13, R.drawable.child_boy, "TOPI", "/to-pi/", "pakaian"),
            FlashcardKosakataItem(14, R.drawable.child_boy, "SEPATU", "/se-pa-tu/", "pakaian"),
            FlashcardKosakataItem(15, R.drawable.child_boy, "KAOS", "/ka-os/", "pakaian")
        )

        val aktivitasData = listOf(
            FlashcardKosakataItem(16, R.drawable.child_boy, "MAKAN", "/ma-kan/", "aktivitas"),
            FlashcardKosakataItem(17, R.drawable.child_boy, "MINUM", "/mi-num/", "aktivitas"),
            FlashcardKosakataItem(18, R.drawable.child_boy, "TIDUR", "/ti-dur/", "aktivitas"),
            FlashcardKosakataItem(19, R.drawable.child_boy, "BERMAIN", "/ber-ma-in/", "aktivitas"),
            FlashcardKosakataItem(20, R.drawable.child_boy, "BELAJAR", "/be-la-jar/", "aktivitas")
        )

        _allFlashcards.value = mapOf(
            "buah" to buahData,
            "hewan" to hewanData,
            "pakaian" to pakaianData,
            "aktivitas" to aktivitasData
        )
    }

    private fun loadCategories() {
        // Memuat kategori untuk ditampilkan di halaman utama
        val categoryList = listOf(
            KosakataExerciseCategory(
                id = 1,
                title = "Mengenal buah",
                description = "Belajar nama-nama buah",
                total = _allFlashcards.value["buah"]?.size ?: 0
            ),
            KosakataExerciseCategory(
                id = 2,
                title = "Mengenal hewan",
                description = "Belajar nama-nama hewan",
                total = _allFlashcards.value["hewan"]?.size ?: 0
            ),
            KosakataExerciseCategory(
                id = 3,
                title = "Mengenal pakaian",
                description = "Belajar nama-nama pakaian",
                total = _allFlashcards.value["pakaian"]?.size ?: 0
            ),
            KosakataExerciseCategory(
                id = 4,
                title = "Mengenal aktivitas",
                description = "Belajar nama-nama aktivitas",
                total = _allFlashcards.value["aktivitas"]?.size ?: 0
            )
        )

        _categories.value = categoryList
    }

    // Set kategori saat ini dan ambil flashcard yang sesuai
    fun setCurrentCategory(category: String) {
        _currentCategory.value = category
        _currentFlashcards.value = _allFlashcards.value[category] ?: emptyList()
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
    }

    // Fungsi untuk mengganti flashcard saat ini
    fun setCurrentIndex(index: Int) {
        if (index >= 0 && index < currentFlashcards.value.size) {
            _currentIndex.value = index
        }
    }

    // Acak kartu dalam kategori saat ini
    fun shuffleCards() {
        _currentFlashcards.value = currentFlashcards.value.shuffled()
        _currentIndex.value = 0
    }

    // Fungsi untuk pindah ke flashcard berikutnya
    fun nextCard() {
        if (currentIndex.value < currentFlashcards.value.size - 1) {
            _currentIndex.value += 1
        }
    }

    // Fungsi untuk menangani jawaban benar
    fun handleCorrectAnswer() {
        _score.value += 1

        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            // Update progress untuk kategori ini
            updateCategoryProgress()
        } else {
            nextCard()
        }
    }

    // Fungsi untuk menangani jawaban salah
    fun handleWrongAnswer() {
        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            // Update progress untuk kategori ini
            updateCategoryProgress()
        } else {
            nextCard()
        }
    }

    // Update progress kategori
    private fun updateCategoryProgress() {
        val categoryIndex = _categories.value.indexOfFirst { it.title.lowercase().contains(currentCategory.value) }
        if (categoryIndex >= 0) {
            val updatedCategories = _categories.value.toMutableList()
            val currentCategory = updatedCategories[categoryIndex]

            // Hitung persentase baru berdasarkan skor
            val newPercentage = (_score.value * 100) / currentFlashcards.value.size

            updatedCategories[categoryIndex] = currentCategory.copy(
                progress = _score.value,
                progressPercentage = newPercentage
            )

            _categories.value = updatedCategories
        }
    }

    fun resetExercise() {
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
    }
}

// Factory untuk membuat instance ViewModel dengan parameter
class KosakataExerciseViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KosakataExerciseViewModel::class.java)) {
            return KosakataExerciseViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}