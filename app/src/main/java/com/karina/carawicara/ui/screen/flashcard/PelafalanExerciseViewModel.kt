package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karina.carawicara.data.FlashcardPelafalanItem
import com.karina.carawicara.data.PelafalanExerciseCategory
import com.karina.carawicara.data.SessionAssessment
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PelafalanExerciseViewModel(
    application: Application,
    private val repository: FlashcardRepository
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<PelafalanExerciseCategory>>(emptyList())
    val categories: StateFlow<List<PelafalanExerciseCategory>> = _categories

    private val _currentCategory = MutableStateFlow("")
    private val currentCategory: StateFlow<String> = _currentCategory

    private val _currentFlashcards = MutableStateFlow<List<FlashcardPelafalanItem>>(emptyList())
    val currentFlashcards: StateFlow<List<FlashcardPelafalanItem>> = _currentFlashcards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _sessionAssessment = MutableStateFlow(SessionAssessment())
    val sessionAssessment: StateFlow<SessionAssessment> = _sessionAssessment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllFlashcards()
    }

    private fun loadAllFlashcards() {
        viewModelScope.launch {
            try {
                Log.d("PelafalanExerciseViewModel", "Memulai loadAllFlashcards")
                val categoryCount = repository.getCategoryCount()
                Log.d("PelafalanExerciseViewModel", "Jumlah kategori di database: $categoryCount")

                if (categoryCount > 0) {
                    repository.getCategoriesByType("konsonan_m").collect { dbCategories ->
                        Log.d("PelafalanExerciseViewModel", "Dapat ${dbCategories.size} kategori konsonan_m")

                        val uiCategories = dbCategories.map { category ->
                            val pelafalanCount = repository.countPelafalanInCategory(category.id)
                            Log.d("PelafalanExerciseViewModel", "Kategori ${category.id} memiliki $pelafalanCount konsonan_m")

                            PelafalanExerciseCategory(
                                id = category.id,
                                title = category.title,
                                description = category.description,
                                total = category.total,
                                progress = category.progress,
                                progressPercentage = category.progressPercentage
                            )
                        }

                        if (uiCategories.isEmpty()) {
                            Log.w("PelafalanExerciseViewModel", "Dapat 0 kategori konsonan_m, menggunakan data dummy")
                            loadDummyCategories()
                        } else {
                            _categories.value = uiCategories
                            Log.d("PelafalanExerciseViewModel", "Berhasil memuat ${uiCategories.size} kategori dari database")
                        }
                    }
                } else {
                    Log.w("PelafalanExerciseViewModel", "Database kosong atau belum terinisialisasi, menggunakan data dummy")
                    loadDummyCategories()
                }
            } catch (e: Exception) {
                Log.e("PelafalanExerciseViewModel", "Error loading categories: ${e.message}", e)
                loadDummyCategories()
            }
        }
    }

    private fun loadDummyCategories() {
        val dummyCategories = listOf(
            PelafalanExerciseCategory(
                id = "konsonan_m",
                title = "Melafalkan konsonan 'm'",
                description = "Belajar melafalkan konsonan 'm' dengan benar.",
                total = 5
            ),
            PelafalanExerciseCategory(
                id = "konsonan_b",
                title = "Melafalkan konsonan 'b'",
                description = "Belajar melafalkan konsonan 'b' dengan benar.",
                total = 5
            ),
        )
        _categories.value = dummyCategories
        Log.d("PelafalanExerciseViewModel", "Loaded ${dummyCategories.size} dummy categories")
    }

    fun setCurrentCategory(category: String) {
        Log.d("PelafalanExerciseViewModel", "Setting current category: $category")
        _currentCategory.value = category
        _errorMessage.value = null

        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false

        viewModelScope.launch {
            try {
                val count = repository.countPelafalanInCategory(category)
                Log.d("PelafalanExerciseViewModel", "Jumlah konsonan_m dalam kategori $category: $count")

                if (count == 0) {
                    _errorMessage.value = "Tidak ada konsonan_m dalam kategori '$category'"
                    _currentFlashcards.value = emptyList()
                    _isLoading.value = false
                    return@launch
                }

                repository.getPelafalanByCategory(category)
                    .catch { e ->
                        Log.e("PelafalanExerciseViewModel", "Error loading from database", e)
                        _errorMessage.value = "Error: ${e.message}"
                        _isLoading.value = false
                        loadFlashcardsFromJson(category)
                    }
                    .collect { entityList ->
                        Log.d("PelafalanExerciseViewModel", "Database returned ${entityList.size} konsonan_m")

                        if (entityList.isNotEmpty()) {
                            val flashcardItems = entityList.map { entity ->
                                Log.d("PelafalanExerciseViewModel", "Mapping: ${entity.word}, ${entity.imageRes}")
                                FlashcardPelafalanItem(
                                    id = entity.id,
                                    imageRes = entity.imageRes,
                                    word = entity.word,
                                    pronunciation = entity.pronunciation,
                                    category = entity.categoryId
                                )
                            }

                            val limitedItems = if (flashcardItems.size > 10) {
                                flashcardItems.shuffled().take(10)
                            } else {
                                flashcardItems
                            }

                            _currentFlashcards.value = limitedItems
                            Log.d("PelafalanExerciseViewModel", "SUCCESS: Set ${limitedItems.size} flashcards")

                            limitedItems.forEachIndexed { index, item ->
                                Log.d("PelafalanExerciseViewModel", "[$index] ${item.word}, ${item.imageRes}")
                            }
                            _isLoading.value = false
                        } else {
                            Log.w("PelafalanExerciseViewModel", "Tidak ada data dari database, coba JSON")
                            _errorMessage.value = "Tidak ada flashcard tersedia di database"
                            loadFlashcardsFromJson(category)
                        }
                    }
            } catch (e: Exception) {
                Log.e("PelafalanExerciseViewModel", "Error umum", e)
                _errorMessage.value = "Error: ${e.message}"
                _isLoading.value = false
                loadFlashcardsFromJson(category)
            }
        }
    }

    private fun loadFlashcardsFromJson(category: String) {
        try {
            val assetManager = getApplication<Application>().assets
            val jsonString = try {
                assetManager.open("flashcards.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                try {
                    assetManager.open("database/pelafalan.json").bufferedReader().use { it.readText() }
                } catch (e2: Exception) {
                    Log.e("PelafalanExerciseViewModel", "Failed to find flashcard JSON in expected locations", e2)
                    loadDummyFlashcards(category)
                    return
                }
            }

            val gson = Gson()

            try {
                val type = object : TypeToken<List<FlashcardPelafalanItem>>() {}.type
                val allItems: List<FlashcardPelafalanItem> = gson.fromJson(jsonString, type)

                val categoryItems = allItems.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    val limitedItems = if (categoryItems.size > 10) {
                        categoryItems.shuffled().take(10)
                    } else {
                        categoryItems
                    }

                    _currentFlashcards.value = limitedItems
                    Log.d("PelafalanExerciseViewModel", "Loaded ${limitedItems.size} flashcards from JSON (direct format)")
                } else {
                    val entityType = object : TypeToken<List<PelafalanEntity>>() {}.type
                    val entityItems: List<PelafalanEntity> = gson.fromJson(jsonString, entityType)

                    val filteredItems = entityItems.filter { it.categoryId == category }
                    if (filteredItems.isNotEmpty()) {
                        val flashcardItems = filteredItems.map { entity ->
                            FlashcardPelafalanItem(
                                id = entity.id,
                                imageRes = entity.imageRes,
                                word = entity.word,
                                pronunciation = entity.pronunciation,
                                category = entity.categoryId
                            )
                        }

                        val limitedItems = if (flashcardItems.size > 10) {
                            flashcardItems.shuffled().take(10)
                        } else {
                            flashcardItems
                        }

                        _currentFlashcards.value = limitedItems
                        Log.d("PelafalanExerciseViewModel", "Loaded ${limitedItems.size} flashcards from JSON (entity format)")
                    } else {
                        _currentFlashcards.value = emptyList()
                        _errorMessage.value = "Tidak ditemukan flashcard untuk kategori $category"
                        Log.e("PelafalanExerciseViewModel", "No flashcards found for category: $category")
                    }
                }
            } catch (e: Exception) {
                Log.e("PelafalanExerciseViewModel", "Error parsing JSON", e)
                loadDummyFlashcards(category)
            }
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("PelafalanExerciseViewModel", "Error loading flashcards from JSON", e)
            _isLoading.value = false
            loadDummyFlashcards(category)
        }
    }

    private fun loadDummyFlashcards(category: String) {
        val dummyFlashcards = when (category) {
            "konsonan_m" -> listOf(
                FlashcardPelafalanItem(1, "images/konsonan_m/ic_masker.png", "MASKER", "masker", "konsonan_m"),
                FlashcardPelafalanItem(2, "images/konsonan_m/ic_mata.png", "MATA", "mata", "konsonan_m"),
                FlashcardPelafalanItem(3, "images/konsonan_m/ic_mulut.png", "MULUT", "mulut", "konsonan_m"),
                FlashcardPelafalanItem(4, "images/konsonan_m/ic_meja.png", "MEJA", "meja", "konsonan_m"),
                FlashcardPelafalanItem(5, "images/konsonan_m/ic_mobil.png", "MOBIL", "mobil", "konsonan_m"),
                FlashcardPelafalanItem(6, "images/konsonan_m/ic_mangga.png", "MANGGA", "mangga", "konsonan_m"),
                FlashcardPelafalanItem(7, "images/konsonan_m/ic_mie.png", "MIE", "mie", "konsonan_m"),
                FlashcardPelafalanItem(8, "images/konsonan_m/ic_monyet.png", "MONYET", "monyet", "konsonan_m"),
                FlashcardPelafalanItem(9, "images/konsonan_m/ic_mainan.png", "MAINAN", "mainan", "konsonan_m"),
                FlashcardPelafalanItem(10, "images/konsonan_m/ic_mangkuk.png", "MANGKUK", "mangkuk", "konsonan_m"),
            ).shuffled().take(10)
            "konsonan_b" -> listOf(
                FlashcardPelafalanItem(1, "images/konsonan_m/ic_baju.png", "BAJU", "baju", "konsonan_b"),
                FlashcardPelafalanItem(2, "images/konsonan_m/ic_balon.png", "BALON", "balon", "konsonan_b"),
                FlashcardPelafalanItem(3, "images/konsonan_m/ic_bantal.png", "BANTAL", "bantal", "konsonan_b"),
                FlashcardPelafalanItem(4, "images/konsonan_m/ic_bel.png", "BEL", "bel", "konsonan_b"),
                FlashcardPelafalanItem(5, "images/konsonan_m/ic_bis.png", "BIS", "bis", "konsonan_b"),
                FlashcardPelafalanItem(6, "images/konsonan_m/ic_bola.png", "BOLA", "bola", "konsonan_b"),
                FlashcardPelafalanItem(7, "images/konsonan_m/ic_boneka.png", "BONEKA", "boneka", "konsonan_b"),
                FlashcardPelafalanItem(8, "images/konsonan_m/ic_botol.png", "BOTOL", "botol", "konsonan_b"),
                FlashcardPelafalanItem(9, "images/konsonan_m/ic_buku.png", "BUKU", "buku", "konsonan_b"),
                FlashcardPelafalanItem(10, "images/konsonan_m/ic_bunga.png", "BUNGA", "bunga", "konsonan_b"),
            ).shuffled().take(10)
            else -> emptyList()
        }

        if (dummyFlashcards.isNotEmpty()) {
            _currentFlashcards.value = dummyFlashcards
            _errorMessage.value = "Menggunakan data dummy untuk kategori $category (mode pengembangan)"
            _isLoading.value = false
            Log.d("PelafalanExerciseViewModel", "Loaded ${dummyFlashcards.size} dummy flashcards for category: $category")
        } else {
            _currentFlashcards.value = emptyList()
            _errorMessage.value = "Tidak ditemukan flashcard untuk kategori $category"
            _isLoading.value = false
            Log.e("PelafalanExerciseViewModel", "No dummy flashcards for category: $category")
        }
    }

    fun shuffleCards() {
        _currentFlashcards.value = currentFlashcards.value.shuffled()
        _currentIndex.value = 0
    }

    fun handleCorrectAnswer() {
        _score.value += 1

        Log.d("PelafalanExerciseViewModel", "Correct answer selected. Current index: ${currentIndex.value}, Score: ${score.value}")

        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            Log.d("PelafalanExerciseViewModel", "Last card completed, exercise marked as completed")
            updateCategoryProgress()
        } else {
            _currentIndex.value += 1
            Log.d("PelafalanExerciseViewModel", "Moving to next card. New index: ${currentIndex.value}")
        }
    }

    fun handleWrongAnswer() {
        Log.d("PelafalanExerciseViewModel", "Wrong answer selected. Current index: ${currentIndex.value}")

        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            Log.d("PelafalanExerciseViewModel", "Last card completed, exercise marked as completed")
            updateCategoryProgress()
        } else {
            _currentIndex.value += 1
            Log.d("PelafalanExerciseViewModel", "Moving to next card. New index: ${currentIndex.value}")
        }
    }

    private fun updateCategoryProgress() {
        viewModelScope.launch {
            try {
                val categoryId = when (currentCategory.value) {
                    "konsonan_m" -> "konsonan_m"
                    else -> currentCategory.value
                }

                repository.updateCategoryProgress(
                    categoryId = categoryId,
                    progress = score.value,
                    total = currentFlashcards.value.size
                )

                val updatedCategories = _categories.value.toMutableList()
                val categoryIndex = updatedCategories.indexOfFirst {
                    it.title.lowercase().contains(currentCategory.value.lowercase())
                }

                if (categoryIndex >= 0) {
                    val currentCategory = updatedCategories[categoryIndex]

                    val newPercentage = (_score.value * 100) / currentFlashcards.value.size

                    updatedCategories[categoryIndex] = currentCategory.copy(
                        progress = _score.value,
                        progressPercentage = newPercentage
                    )

                    _categories.value = updatedCategories
                    Log.d("PelafalanExerciseViewModel", "Updated progress for category ${currentCategory.title}: ${_score.value}/${currentFlashcards.value.size} (${newPercentage}%)")
                }
            } catch (e: Exception) {
                Log.e("PelafalanExerciseViewModel", "Error updating category progress", e)
            }
        }
    }

    fun resetExercise() {
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
        _errorMessage.value = null
    }

    fun logDebugInfo() {
        Log.d("PelafalanExerciseViewModel", "==== DEBUG INFO ====")
        Log.d("PelafalanExerciseViewModel", "Current Category: ${currentCategory.value}")
        Log.d("PelafalanExerciseViewModel", "Category Count: ${categories.value.size}")
        Log.d("PelafalanExerciseViewModel", "Categories: ${categories.value.map { it.title }}")
        Log.d("PelafalanExerciseViewModel", "Current Flashcards Count: ${currentFlashcards.value.size}")
        Log.d("PelafalanExerciseViewModel", "Current Index: ${currentIndex.value}")
        Log.d("PelafalanExerciseViewModel", "Error Message: ${errorMessage.value}")

        try {
            val assetManager = getApplication<Application>().assets
            Log.d("PelafalanExerciseViewModel", "Root Assets:")
            assetManager.list("")?.forEach { Log.d("PelafalanExerciseViewModel", "- $it") }

            if (assetManager.list("")?.contains("database") == true) {
                Log.d("PelafalanExerciseViewModel", "Database Assets:")
                assetManager.list("database")?.forEach {
                    Log.d("PelafalanExerciseViewModel", "- database/$it")
                }
            }

            if (assetManager.list("")?.contains("images") == true) {
                Log.d("PelafalanExerciseViewModel", "Image Assets:")
                assetManager.list("images")?.forEach {
                    Log.d("PelafalanExerciseViewModel", "- images/konsonan_m/$it")
                }
            }
        } catch (e: Exception) {
            Log.e("PelafalanExerciseViewModel", "Error listing assets", e)
        }

        Log.d("PelafalanExerciseViewModel", "==== END DEBUG INFO ====")
    }

    fun moveToNextCard() {
        if (currentIndex.value < currentFlashcards.value.size - 1) {
            _currentIndex.value += 1
            Log.d("PelafalanExerciseViewModel", "Moved to next card: ${currentIndex.value}")
        } else {
            Log.d("PelafalanExerciseViewModel", "Already at the last card, cannot move forward")
        }
    }

    fun resetLoadingState() {
        _isLoading.value = false
    }
}

class PelafalanExerciseViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PelafalanExerciseViewModel::class.java)) {
            return PelafalanExerciseViewModel(
                application,
                AppModule.provideFlashcardRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}