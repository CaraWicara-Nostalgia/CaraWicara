package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karina.carawicara.data.FlashcardKosakataItem
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class KosakataExerciseViewModel(
    application: Application,
    private val repository: FlashcardRepository
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<KosakataExerciseCategory>>(emptyList())
    val categories: StateFlow<List<KosakataExerciseCategory>> = _categories

    private val _currentCategory = MutableStateFlow("")
    private val currentCategory: StateFlow<String> = _currentCategory

    private val _currentFlashcards = MutableStateFlow<List<FlashcardKosakataItem>>(emptyList())
    val currentFlashcards: StateFlow<List<FlashcardKosakataItem>> = _currentFlashcards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadAllFlashcards()
    }

    private fun loadAllFlashcards() {
        viewModelScope.launch {
            try {
                Log.d("KosakataExerciseViewModel", "Memulai loadAllFlashcards")
                val categoryCount = repository.getCategoryCount()
                Log.d("KosakataExerciseViewModel", "Jumlah kategori di database: $categoryCount")

                if (categoryCount > 0) {
                    repository.getCategoriesByType("kosakata").collect { dbCategories ->
                        Log.d("KosakataExerciseViewModel", "Dapat ${dbCategories.size} kategori kosakata")

                        val uiCategories = dbCategories.map { category ->
                            val kosakataCount = repository.countKosakataInCategory(category.id)
                            Log.d("KosakataExerciseViewModel", "Kategori ${category.id} memiliki $kosakataCount kosakata")

                            KosakataExerciseCategory(
                                id = category.id,
                                title = category.title,
                                description = category.description,
                                total = category.total,
                                progress = category.progress,
                                progressPercentage = category.progressPercentage
                            )
                        }

                        if (uiCategories.isEmpty()) {
                            Log.w("KosakataExerciseViewModel", "Dapat 0 kategori kosakata, menggunakan data dummy")
                            loadDummyCategories()
                        } else {
                            _categories.value = uiCategories
                            Log.d("KosakataExerciseViewModel", "Berhasil memuat ${uiCategories.size} kategori dari database")
                        }
                    }
                } else {
                    Log.w("KosakataExerciseViewModel", "Database kosong atau belum terinisialisasi, menggunakan data dummy")
                    loadDummyCategories()
                }
            } catch (e: Exception) {
                Log.e("KosakataExerciseViewModel", "Error loading categories: ${e.message}", e)
                loadDummyCategories()
            }
        }
    }

    private fun loadDummyCategories() {
        val dummyCategories = listOf(
            KosakataExerciseCategory(
                id = "buah",
                title = "Mengenal buah",
                description = "Belajar nama-nama buah",
                total = 5
            ),
            KosakataExerciseCategory(
                id = "hewan",
                title = "Mengenal hewan",
                description = "Belajar nama-nama hewan",
                total = 5
            ),
            KosakataExerciseCategory(
                id = "pakaian",
                title = "Mengenal pakaian",
                description = "Belajar nama-nama pakaian",
                total = 5
            ),
            KosakataExerciseCategory(
                id = "aktivitas",
                title = "Mengenal aktivitas",
                description = "Belajar nama-nama aktivitas",
                total = 5
            )
        )
        _categories.value = dummyCategories
        Log.d("KosakataExerciseViewModel", "Loaded ${dummyCategories.size} dummy categories")
    }

    fun setCurrentCategory(category: String) {
        Log.d("KosakataExerciseViewModel", "Setting current category: $category")
        _currentCategory.value = category
        _errorMessage.value = null

        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false

        viewModelScope.launch {
            try {
                val count = repository.countKosakataInCategory(category)
                Log.d("KosakataExerciseViewModel", "Jumlah kosakata dalam kategori $category: $count")

                if (count == 0) {
                    _errorMessage.value = "Tidak ada kosakata dalam kategori '$category'"
                    _currentFlashcards.value = emptyList()
                    return@launch
                }
                repository.getKosakataByCategory(category)
                    .catch { e ->
                        Log.e("KosakataExerciseViewModel", "Error loading from database", e)
                        _errorMessage.value = "Error: ${e.message}"
                        loadFlashcardsFromJson(category)
                    }
                    .collect { entityList ->
                        Log.d("KosakataExerciseViewModel", "Database returned ${entityList.size} kosakata")

                        if (entityList.isNotEmpty()) {
                            val flashcardItems = entityList.map { entity ->
                                Log.d("KosakataExerciseViewModel", "Mapping: ${entity.word}, ${entity.imageRes}")
                                FlashcardKosakataItem(
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
                            Log.d("KosakataExerciseViewModel", "SUCCESS: Set ${limitedItems.size} flashcards")

                            limitedItems.forEachIndexed { index, item ->
                                Log.d("KosakataExerciseViewModel", "[$index] ${item.word}, ${item.imageRes}")
                            }
                        } else {
                            Log.w("KosakataExerciseViewModel", "Tidak ada data dari database, coba JSON")
                            _errorMessage.value = "Tidak ada flashcard tersedia di database"
                            loadFlashcardsFromJson(category)
                        }
                    }
            } catch (e: Exception) {
                Log.e("KosakataExerciseViewModel", "Error umum", e)
                _errorMessage.value = "Error: ${e.message}"
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
                    assetManager.open("database/kosakata.json").bufferedReader().use { it.readText() }
                } catch (e2: Exception) {
                    Log.e("KosakataExerciseViewModel", "Failed to find flashcard JSON in expected locations", e2)
                    loadDummyFlashcards(category)
                    return
                }
            }

            val gson = Gson()

            try {
                val type = object : TypeToken<List<FlashcardKosakataItem>>() {}.type
                val allItems: List<FlashcardKosakataItem> = gson.fromJson(jsonString, type)

                val categoryItems = allItems.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    val limitedItems = if (categoryItems.size > 10) {
                        categoryItems.shuffled().take(10)
                    } else {
                        categoryItems
                    }

                    _currentFlashcards.value = limitedItems
                    Log.d("KosakataExerciseViewModel", "Loaded ${limitedItems.size} flashcards from JSON (direct format)")
                } else {
                    val entityType = object : TypeToken<List<KosakataEntity>>() {}.type
                    val entityItems: List<KosakataEntity> = gson.fromJson(jsonString, entityType)

                    val filteredItems = entityItems.filter { it.categoryId == category }
                    if (filteredItems.isNotEmpty()) {
                        val flashcardItems = filteredItems.map { entity ->
                            FlashcardKosakataItem(
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
                        Log.d("KosakataExerciseViewModel", "Loaded ${limitedItems.size} flashcards from JSON (entity format)")
                    } else {
                        _currentFlashcards.value = emptyList()
                        _errorMessage.value = "Tidak ditemukan flashcard untuk kategori $category"
                        Log.e("KosakataExerciseViewModel", "No flashcards found for category: $category")
                    }
                }
            } catch (e: Exception) {
                Log.e("KosakataExerciseViewModel", "Error parsing JSON", e)
                loadDummyFlashcards(category)
            }
        } catch (e: Exception) {
            Log.e("KosakataExerciseViewModel", "Error loading flashcards from JSON", e)
            loadDummyFlashcards(category)
        }
    }

     private fun loadDummyFlashcards(category: String) {
        val dummyFlashcards = when (category) {
            "buah" -> listOf(
                FlashcardKosakataItem(1, "images/buah/ic_anggur.png", "ANGGUR", "aŋɡur", "buah"),
                FlashcardKosakataItem(2, "images/buah/ic_apel.png", "APEL", "apəl", "buah"),
                FlashcardKosakataItem(3, "images/buah/ic_jeruk.png", "JERUK", "jəruk", "buah"),
                FlashcardKosakataItem(4, "images/buah/ic_mangga.png", "MANGGA", "maŋɡa", "buah"),
                FlashcardKosakataItem(5, "images/buah/ic_pisang.png", "PISANG", "pisaŋ", "buah"),
                FlashcardKosakataItem(6, "images/buah/ic_semangka.png", "SEMANGKA", "səmaŋka", "buah"),
                FlashcardKosakataItem(7, "images/buah/ic_strawberry.png", "STROBERI", "strobəri", "buah"),
                FlashcardKosakataItem(8, "images/buah/ic_alpukat.png", "ALPUKAT", "alpukat", "buah"),
                FlashcardKosakataItem(9, "images/buah/ic_nanas.png", "NANAS", "nanas", "buah"),
                FlashcardKosakataItem(10, "images/buah/ic_melon.png", "MELON", "məlon", "buah")
            ).shuffled().take(10)
            "hewan" -> listOf(
                FlashcardKosakataItem(6, "images/hewan/ic_kucing.png", "KUCING", "kuciŋ", "hewan"),
                FlashcardKosakataItem(7, "images/hewan/ic_anjing.png", "ANJING", "anjiŋ", "hewan"),
                FlashcardKosakataItem(8, "images/hewan/ic_sapi.png", "SAPI", "sapi", "hewan"),
                FlashcardKosakataItem(9, "images/hewan/ic_ayam.png", "AYAM", "ayam", "hewan"),
                FlashcardKosakataItem(10, "images/hewan/ic_ikan.png", "IKAN", "ikan", "hewan"),
                FlashcardKosakataItem(11, "images/hewan/ic_buaya.png", "BUAYA", "buaya", "hewan"),
                FlashcardKosakataItem(12, "images/hewan/ic_ular.png", "ULAR", "ular", "hewan"),
                FlashcardKosakataItem(13, "images/hewan/ic_singa.png", "SINGA", "siŋa", "hewan"),
                FlashcardKosakataItem(14, "images/hewan/ic_gajah.png", "GAJAH", "ɡajah", "hewan"),
                FlashcardKosakataItem(15, "images/hewan/ic_monyet.png", "MONYET", "monyət", "hewan")
            ).shuffled().take(10)
            "pakaian" -> listOf(
                FlashcardKosakataItem(11, "images/pakaian/ic_celana.png", "CELANA", "cəlana", "pakaian"),
                FlashcardKosakataItem(12, "images/pakaian/ic_jaket.png", "JAKET", "jakət", "pakaian"),
                FlashcardKosakataItem(13, "images/pakaian/ic_kaos.png", "KAOS", "kaos", "pakaian"),
                FlashcardKosakataItem(14, "images/pakaian/ic_sepatu.png", "SEPATU", "səpatu", "pakaian"),
                FlashcardKosakataItem(15, "images/pakaian/ic_topi.png", "TOPI", "topi", "pakaian"),
                FlashcardKosakataItem(16, "images/pakaian/ic_sandal.png", "SANDAL", "sandal", "pakaian"),
                FlashcardKosakataItem(17, "images/pakaian/ic_kaoskaki.png", "KAOS KAKI", "kaos kaki", "pakaian"),
                FlashcardKosakataItem(18, "images/pakaian/ic_diapers.png", "PAMPERS", "pampərs", "pakaian")
            ).shuffled().take(8)
            "aktivitas" -> listOf(
                FlashcardKosakataItem(16, "images/aktivitas/ic_makan.png", "MAKAN", "makan", "aktivitas"),
                FlashcardKosakataItem(17, "images/aktivitas/ic_minum.png", "MINUM", "minum", "aktivitas"),
                FlashcardKosakataItem(18, "images/aktivitas/ic_tidur.png", "TIDUR", "tidur", "aktivitas"),
                FlashcardKosakataItem(19, "images/aktivitas/ic_mandi.png", "MANDI", "mandi", "aktivitas"),
                FlashcardKosakataItem(20, "images/aktivitas/ic_memasak.png", "MEMASAK", "məmasak", "aktivitas"),
                FlashcardKosakataItem(21, "images/aktivitas/ic_menelepon.png", "MENELEPON", "mənələpon", "aktivitas"),
                FlashcardKosakataItem(22, "images/aktivitas/ic_menyapu.png", "MENYAPU", "məɲapu", "aktivitas"),
                FlashcardKosakataItem(23, "images/aktivitas/ic_gosokgigi.png", "GOSOK GIGI", "ɡosok ɡiɡi", "aktivitas"),
                FlashcardKosakataItem(24, "images/aktivitas/ic_cucitanangan.png", "CUCI TANGAN", "cuci taŋan", "aktivitas"),
                FlashcardKosakataItem(25, "images/aktivitas/ic_mencucipiring.png", "MENCUCI PIRING", "məncuci piriŋ", "aktivitas")
            ).shuffled().take(10)
            else -> emptyList()
        }

        if (dummyFlashcards.isNotEmpty()) {
            _currentFlashcards.value = dummyFlashcards
            _errorMessage.value = "Menggunakan data dummy untuk kategori $category (mode pengembangan)"
            Log.d("KosakataExerciseViewModel", "Loaded ${dummyFlashcards.size} dummy flashcards for category: $category")
        } else {
            _currentFlashcards.value = emptyList()
            _errorMessage.value = "Tidak ditemukan flashcard untuk kategori $category"
            Log.e("KosakataExerciseViewModel", "No dummy flashcards for category: $category")
        }
    }

    fun shuffleCards() {
        _currentFlashcards.value = currentFlashcards.value.shuffled()
        _currentIndex.value = 0
    }

    fun handleCorrectAnswer() {
        _score.value += 1

        Log.d("KosakataExerciseViewModel", "Correct answer selected. Current index: ${currentIndex.value}, Score: ${score.value}")

        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            Log.d("KosakataExerciseViewModel", "Last card completed, exercise marked as completed")
            updateCategoryProgress()
        } else {
            _currentIndex.value += 1
            Log.d("KosakataExerciseViewModel", "Moving to next card. New index: ${currentIndex.value}")
        }
    }

    fun handleWrongAnswer() {
        Log.d("KosakataExerciseViewModel", "Wrong answer selected. Current index: ${currentIndex.value}")

        if (currentIndex.value >= currentFlashcards.value.size - 1) {
            _isExerciseCompleted.value = true
            Log.d("KosakataExerciseViewModel", "Last card completed, exercise marked as completed")
            updateCategoryProgress()
        } else {
            _currentIndex.value += 1
            Log.d("KosakataExerciseViewModel", "Moving to next card. New index: ${currentIndex.value}")
        }
    }

    private fun updateCategoryProgress() {
        viewModelScope.launch {
            try {
                val categoryId = when (currentCategory.value) {
                    "buah" -> "buah"
                    "hewan" -> "hewan"
                    "pakaian" -> "pakaian"
                    "aktivitas" -> "aktivitas"
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
                    Log.d("KosakataExerciseViewModel", "Updated progress for category ${currentCategory.title}: ${_score.value}/${currentFlashcards.value.size} (${newPercentage}%)")
                }
            } catch (e: Exception) {
                Log.e("KosakataExerciseViewModel", "Error updating category progress", e)
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
        Log.d("KosakataExerciseViewModel", "==== DEBUG INFO ====")
        Log.d("KosakataExerciseViewModel", "Current Category: ${currentCategory.value}")
        Log.d("KosakataExerciseViewModel", "Category Count: ${categories.value.size}")
        Log.d("KosakataExerciseViewModel", "Categories: ${categories.value.map { it.title }}")
        Log.d("KosakataExerciseViewModel", "Current Flashcards Count: ${currentFlashcards.value.size}")
        Log.d("KosakataExerciseViewModel", "Current Index: ${currentIndex.value}")
        Log.d("KosakataExerciseViewModel", "Error Message: ${errorMessage.value}")

        try {
            val assetManager = getApplication<Application>().assets
            Log.d("KosakataExerciseViewModel", "Root Assets:")
            assetManager.list("")?.forEach { Log.d("KosakataExerciseViewModel", "- $it") }

            if (assetManager.list("")?.contains("database") == true) {
                Log.d("KosakataExerciseViewModel", "Database Assets:")
                assetManager.list("database")?.forEach {
                    Log.d("KosakataExerciseViewModel", "- database/$it")
                }
            }

            if (assetManager.list("")?.contains("images") == true) {
                Log.d("KosakataExerciseViewModel", "Image Assets:")
                assetManager.list("images")?.forEach {
                    Log.d("KosakataExerciseViewModel", "- images/$it")
                }
            }
        } catch (e: Exception) {
            Log.e("KosakataExerciseViewModel", "Error listing assets", e)
        }

        Log.d("KosakataExerciseViewModel", "==== END DEBUG INFO ====")
    }
}

data class KosakataExerciseCategory(
    val id: String,
    val title: String,
    val description: String = "",
    val progress: Int = 0,
    val progressPercentage: Int = 0,
    val total: Int = 0
)

class KosakataExerciseViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KosakataExerciseViewModel::class.java)) {
            return KosakataExerciseViewModel(
                application,
                AppModule.provideFlashcardRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}