package com.karina.carawicara.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karina.carawicara.R
import com.karina.carawicara.data.SequenceExerciseCategory
import com.karina.carawicara.data.SequenceExerciseItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SequenceExerciseViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<SequenceExerciseCategory>>(emptyList())
    val categories: StateFlow<List<SequenceExerciseCategory>> = _categories

    // Kategori yang sedang aktif
    private val _currentCategory = MutableStateFlow<String>("")
    val currentCategory: StateFlow<String> = _currentCategory

    // Data latihan urutan untuk kategori yang aktif
    private val _sequenceItems = MutableStateFlow<List<SequenceExerciseItem>>(emptyList())
    val sequenceItems: StateFlow<List<SequenceExerciseItem>> = _sequenceItems

    // Array yang menyimpan urutan gambar yang dipilih oleh pengguna
    private val _selectedImages = MutableStateFlow<List<Int?>>(listOf(null, null, null, null))
    val selectedImages: StateFlow<List<Int?>> = _selectedImages

    // Index urutan saat ini
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    // Skor saat ini untuk kategori aktif
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    // Status penyelesaian latihan
    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    init {
        loadCategories()
        loadSequenceItems()
    }

    private fun loadCategories() {
        // Memuat kategori untuk ditampilkan di halaman utama
        val categoryList = listOf(
            SequenceExerciseCategory(
                id = 1,
                title = "Mengurutkan aktivitas",
                description = "Belajar mengurutkan aktivitas sehari-hari",
                total = 3,
                progress = 2,
                progressPercentage = 75
            )
        )

        _categories.value = categoryList
    }

    private fun loadSequenceItems() {
        // Data latihan urutan
        val sequenceData = listOf(
            SequenceExerciseItem(
                id = 1,
                title = "Pilih gambar sesuai urutan yang benar",
                images = listOf(R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy),
                correctOrder = listOf(0, 1, 2, 3),
                category = "aktivitas"
            ),
            SequenceExerciseItem(
                id = 2,
                title = "Pilih gambar sesuai urutan yang benar",
                images = listOf(R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy),
                correctOrder = listOf(0, 1, 2, 3),
                category = "aktivitas"
            ),
            SequenceExerciseItem(
                id = 3,
                title = "Pilih gambar sesuai urutan yang benar",
                images = listOf(R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy, R.drawable.child_boy),
                correctOrder = listOf(0, 1, 2, 3),
                category = "aktivitas"
            )
        )

        _sequenceItems.value = sequenceData
    }

    fun setCurrentCategory(category: String) {
        _currentCategory.value = category
        resetExercise()
    }

    fun resetExercise() {
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
        resetSelections()
    }

    // Fungsi untuk menangani ketika gambar dipilih
    fun selectImage(imageIndex: Int) {
        val currentSelections = _selectedImages.value.toMutableList()
        val firstEmptySlot = currentSelections.indexOfFirst { it == null }

        // Jika ada slot kosong, letakkan gambar di sana
        if (firstEmptySlot != -1) {
            currentSelections[firstEmptySlot] = imageIndex
            _selectedImages.value = currentSelections
        }
    }

    // Fungsi untuk mengosongkan slot yang dipilih
    fun clearSelection(slotIndex: Int) {
        val currentSelections = _selectedImages.value.toMutableList()
        if (slotIndex >= 0 && slotIndex < currentSelections.size) {
            currentSelections[slotIndex] = null
            _selectedImages.value = currentSelections
        }
    }

    // Fungsi untuk mengosongkan semua pilihan
    fun resetSelections() {
        _selectedImages.value = listOf(null, null, null, null)
    }

    // Fungsi untuk memeriksa apakah urutan sudah benar
    fun checkOrder(): Boolean {
        val currentSelections = _selectedImages.value
        val correctOrder = _sequenceItems.value[_currentIndex.value].correctOrder

        // Pastikan semua slot terisi
        if (currentSelections.contains(null)) {
            return false
        }

        // Bandingkan dengan urutan yang benar
        for (i in currentSelections.indices) {
            val selectedImageIndex = currentSelections[i] ?: continue
            if (selectedImageIndex != correctOrder[i]) {
                return false
            }
        }

        return true
    }

    // Fungsi untuk memeriksa jawaban dan lanjut ke soal berikutnya
    fun submitAnswer() {
        if (checkOrder()) {
            _score.value += 1
        }

        if (_currentIndex.value < _sequenceItems.value.size - 1) {
            // Lanjut ke soal berikutnya
            _currentIndex.value += 1
            resetSelections()
        } else {
            // Latihan selesai
            _isExerciseCompleted.value = true
            // Update progress untuk kategori ini
            updateCategoryProgress()
        }
    }

    // Fungsi untuk menangani jawaban manual
    fun handleCorrectAnswer() {
        _score.value += 1
        if (_currentIndex.value < _sequenceItems.value.size - 1) {
            // Lanjut ke soal berikutnya
            _currentIndex.value += 1
            resetSelections()
        } else {
            // Latihan selesai
            _isExerciseCompleted.value = true
            // Update progress untuk kategori ini
            updateCategoryProgress()
        }
    }

    // Fungsi untuk menangani jawaban salah
    fun handleWrongAnswer() {
        if (_currentIndex.value < _sequenceItems.value.size - 1) {
            // Lanjut ke soal berikutnya
            _currentIndex.value += 1
            resetSelections()
        } else {
            // Latihan selesai
            _isExerciseCompleted.value = true
            // Update progress untuk kategori ini
            updateCategoryProgress()
        }
    }

    // Update progress kategori
    private fun updateCategoryProgress() {
        val categoryIndex = 0 // Karena hanya ada 1 kategori
        if (_categories.value.isNotEmpty()) {
            val updatedCategories = _categories.value.toMutableList()
            val currentCategory = updatedCategories[categoryIndex]

            // Hitung persentase baru berdasarkan skor
            val newPercentage = (_score.value * 100) / _sequenceItems.value.size

            updatedCategories[categoryIndex] = currentCategory.copy(
                progress = _score.value,
                progressPercentage = newPercentage
            )

            _categories.value = updatedCategories
        }
    }
}

// Factory untuk membuat instance ViewModel dengan parameter
class SequenceExerciseViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SequenceExerciseViewModel::class.java)) {
            return SequenceExerciseViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}