package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karina.carawicara.data.SequenceExerciseCategory
import com.karina.carawicara.data.SequenceExerciseItem
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SequenceExerciseViewModel(
    private val navController: NavController,
    application: Application,
    private val repository: FlashcardRepository
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<SequenceExerciseCategory>>(emptyList())
    val categories: StateFlow<List<SequenceExerciseCategory>> = _categories

    private val _currentCategory = MutableStateFlow("")
    val currentCategory: StateFlow<String> = _currentCategory

    private val _sequenceItems = MutableStateFlow<List<SequenceExerciseItem>>(emptyList())
    val sequenceItems: StateFlow<List<SequenceExerciseItem>> = _sequenceItems

    private val _selectedImages = MutableStateFlow<List<Int?>>(listOf(null, null, null, null))
    val selectedImages: StateFlow<List<Int?>> = _selectedImages

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isExerciseCompleted = MutableStateFlow(false)
    val isExerciseCompleted: StateFlow<Boolean> = _isExerciseCompleted

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadAllCategories()
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            try {
                Log.d("SequenceExerciseViewModel", "Memulai loadAllCategories")
                val categoryCount = repository.getCategoryCount()
                Log.d("SequenceExerciseViewModel", "Jumlah kategori di database: $categoryCount")

                if (categoryCount > 0) {
                    repository.getCategoriesByType("sequence").collect { dbCategories ->
                        Log.d(
                            "SequenceExerciseViewModel",
                            "Dapat ${dbCategories.size} kategori sequence"
                        )

                        val uiCategories = dbCategories.map { category ->
                            val sequenceCount = repository.countSequenceInCategory(category.id)
                            Log.d(
                                "SequenceExerciseViewModel",
                                "Kategori ${category.id} memiliki $sequenceCount sequence"
                            )

                            SequenceExerciseCategory(
                                id = category.id.hashCode(), // Konversi String ID ke Int
                                title = category.title,
                                description = category.description,
                                total = category.total,
                                progress = category.progress,
                                progressPercentage = category.progressPercentage
                            )
                        }

                        if (uiCategories.isEmpty()) {
                            Log.w(
                                "SequenceExerciseViewModel",
                                "Dapat 0 kategori sequence, menggunakan data dummy"
                            )
                            loadDummyCategories()
                        } else {
                            _categories.value = uiCategories
                            Log.d(
                                "SequenceExerciseViewModel",
                                "Berhasil memuat ${uiCategories.size} kategori dari database"
                            )
                        }
                    }
                } else {
                    Log.w(
                        "SequenceExerciseViewModel",
                        "Database kosong atau belum terinisialisasi, menggunakan data dummy"
                    )
                    loadDummyCategories()
                }
            } catch (e: Exception) {
                Log.e("SequenceExerciseViewModel", "Error loading categories: ${e.message}", e)
                loadDummyCategories()
            }
        }
    }

    private fun loadDummyCategories() {
        val dummyCategories = listOf(
            SequenceExerciseCategory(
                id = 1,
                title = "Mengurutkan aktivitas",
                description = "Belajar mengurutkan aktivitas sehari-hari",
                total = 3,
                progress = 2,
                progressPercentage = 75
            )
        )
        _categories.value = dummyCategories
        Log.d("SequenceExerciseViewModel", "Loaded ${dummyCategories.size} dummy categories")
    }

    fun setCurrentCategory(category: String) {
        Log.d("SequenceExerciseViewModel", "Setting current category: $category")
        _currentCategory.value = category
        _errorMessage.value = null

        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
        resetSelections()

        viewModelScope.launch {
            try {
                when (category) {
                    "aktivitas_urutan" -> {
                        loadSequencesForCategory(category)
                    }

                    else -> {
                        loadSequencesForCategory(category)
                    }
                }
            } catch (e: Exception) {
                Log.e("SequenceExerciseViewModel", "Error umum", e)
                _errorMessage.value = "Error: ${e.message}"
                loadDummySequenceItems()
            }
        }
    }

    private suspend fun loadSequencesForCategory(category: String) {
        try {
            val count = repository.countSequenceInCategory(category)
            Log.d("SequenceExerciseViewModel", "Jumlah sequence dalam kategori $category: $count")

            if (count == 0) {
                Log.w("SequenceExerciseViewModel", "No sequences in category: $category")
                _errorMessage.value = "Tidak ada sequence dalam kategori '$category'"
                _sequenceItems.value = emptyList()
                loadDummySequenceItems()
                return
            }

            repository.getSequenceByCategory(category)
                .catch { e ->
                    Log.e("SequenceExerciseViewModel", "Error loading from database", e)
                    _errorMessage.value = "Error: ${e.message}"
                    loadDummySequenceItems()
                }
                .collect { entityList ->
                    Log.d(
                        "SequenceExerciseViewModel",
                        "Database returned ${entityList.size} sequence"
                    )

                    if (entityList.isNotEmpty()) {
                        val sequenceItems = entityList.mapIndexed { index, entity ->
                            try {
                                val gson = Gson()

                                val imageResourcesType = object : TypeToken<List<String>>() {}.type
                                val imageResources: List<String> =
                                    gson.fromJson(entity.imageResourcesJson, imageResourcesType)
                                Log.d(
                                    "SequenceExerciseViewModel",
                                    "Parsed image resources: $imageResources"
                                )

                                val correctOrderType = object : TypeToken<List<Int>>() {}.type
                                val correctOrderList: List<Int> =
                                    gson.fromJson(entity.correctOrderJson, correctOrderType)

                                val correctOrderMap =
                                    correctOrderList.withIndex().associate { (index, value) ->
                                        index to value
                                    }
                                Log.d(
                                    "SequenceExerciseViewModel",
                                    "Correct order map: $correctOrderMap"
                                )

                                SequenceExerciseItem(
                                    id = index + 1,
                                    title = entity.title,
                                    images = imageResources,
                                    correctOrder = correctOrderMap,
                                    category = entity.categoryId
                                )
                            } catch (e: Exception) {
                                Log.e(
                                    "SequenceExerciseViewModel",
                                    "Error parsing entity: ${entity.title}",
                                    e
                                )
                                null
                            }
                        }.filterNotNull()

                        val limitedItems = if (sequenceItems.size > 5) {
                            sequenceItems.shuffled().take(5)
                        } else {
                            sequenceItems
                        }

                        if (limitedItems.isNotEmpty()) {
                            _sequenceItems.value = limitedItems
                            Log.d(
                                "SequenceExerciseViewModel",
                                "Set ${limitedItems.size} sequence items"
                            )

                            val firstItem = limitedItems.first()
                            _selectedImages.value = List(firstItem.images.size) { null }

                            fixAllSequenceImages()

                            shuffleCurrentSequence()
                        } else {
                            Log.w(
                                "SequenceExerciseViewModel",
                                "No valid sequence items after parsing"
                            )
                            _errorMessage.value = "Tidak ada sequence valid tersedia"
                            loadDummySequenceItems()
                        }
                    } else {
                        Log.w("SequenceExerciseViewModel", "No sequence data from database")
                        _errorMessage.value = "Tidak ada sequence tersedia di database"
                        loadDummySequenceItems()
                    }
                }
        } catch (e: Exception) {
            Log.e("SequenceExerciseViewModel", "Unexpected error", e)
            _errorMessage.value = "Error: ${e.message}"
            loadDummySequenceItems()
        }
    }

    private fun loadDummySequenceItems() {
        val category = _currentCategory.value
        Log.d("SequenceExerciseViewModel", "Loading dummy items for category: $category")

        val dummyItems = when (category) {
            "aktivitas_urutan" -> {
                listOf(
                    createDummySequence(
                        id = 1,
                        title = "Pilih gambar sesuai urutan: Sikat Gigi",
                        imagePaths = listOf(
                            "images/sequence/brush_teeth_1.png",
                            "images/sequence/brush_teeth_2.png",
                            "images/sequence/brush_teeth_3.png",
                            "images/sequence/brush_teeth_4.png"
                        ),
                        category = "aktivitas_urutan"
                    ),
                    createDummySequence(
                        id = 2,
                        title = "Pilih gambar sesuai urutan: Cuci Tangan",
                        imagePaths = listOf(
                            "images/sequence/wash_hand_1.png",
                            "images/sequence/wash_hand_2.png",
                            "images/sequence/wash_hand_3.png",
                            "images/sequence/wash_hand_4.png"
                        ),
                        category = "aktivitas_urutan"
                    ),
                    createDummySequence(
                        id = 3,
                        title = "Pilih gambar sesuai urutan: Makan",
                        imagePaths = listOf(
                            "images/sequence/eating_1.png",
                            "images/sequence/eating_2.png",
                            "images/sequence/eating_3.png",
                            "images/sequence/eating_4.png"
                        ),
                        category = "aktivitas_urutan"
                    )
                )
            }

            else -> {
                // Default fallback
                listOf(
                    createDummySequence(
                        id = 1,
                        title = "Pilih gambar sesuai urutan: Aktivitas",
                        imagePaths = listOf(
                            "images/sequence/activity_1.png",
                            "images/sequence/activity_2.png",
                            "images/sequence/activity_3.png",
                            "images/sequence/activity_4.png"
                        ),
                        category = category
                    )
                )
            }
        }

        _sequenceItems.value = dummyItems

        if (dummyItems.isNotEmpty()) {
            val firstItem = dummyItems.first()
            _selectedImages.value = List(firstItem.images.size) { null }
        } else {
            _selectedImages.value = List(4) { null }
        }

        fixAllSequenceImages()

        shuffleCurrentSequence()
    }

    private fun createDummySequence(
        id: Int,
        title: String,
        imagePaths: List<String>,
        category: String
    ): SequenceExerciseItem {
        val correctOrder = imagePaths.indices.associateWith { it }

        return SequenceExerciseItem(
            id = id,
            title = title,
            images = imagePaths,
            correctOrder = correctOrder,
            category = category
        )
    }

    fun getCorrectImagePathsForTitle(title: String): List<String> {
        return when {
            title.contains("Cuci Tangan", ignoreCase = true) -> {
                listOf(
                    "images/sequence/wash_hand_1.png",
                    "images/sequence/wash_hand_2.png",
                    "images/sequence/wash_hand_3.png",
                    "images/sequence/wash_hand_4.png"
                )
            }

            title.contains("Sikat Gigi", ignoreCase = true) -> {
                listOf(
                    "images/sequence/brush_teeth_1.png",
                    "images/sequence/brush_teeth_2.png",
                    "images/sequence/brush_teeth_3.png",
                    "images/sequence/brush_teeth_4.png"
                )
            }

            title.contains("Makan", ignoreCase = true) -> {
                listOf(
                    "images/sequence/eating_1.png",
                    "images/sequence/eating_2.png",
                    "images/sequence/eating_3.png",
                    "images/sequence/eating_4.png"
                )
            }

            else -> {
                listOf(
                    "images/sequence/activity_1.png",
                    "images/sequence/activity_2.png",
                    "images/sequence/activity_3.png",
                    "images/sequence/activity_4.png"
                )
            }
        }
    }

    private fun fixAllSequenceImages() {
        val updatedItems = _sequenceItems.value.mapIndexed { _, item ->
            val correctImages = getCorrectImagePathsForTitle(item.title)

            if (item.images != correctImages) {
                Log.d("SequenceExerciseViewModel", "Fixing images for item: ${item.title}")
                Log.d("SequenceExerciseViewModel", "Original images: ${item.images}")
                Log.d("SequenceExerciseViewModel", "Correct images: $correctImages")

                item.copy(
                    images = correctImages,
                    correctOrder = correctImages.indices.associateWith { it }
                )
            } else {
                item
            }
        }

        _sequenceItems.value = updatedItems
    }

    fun fixCurrentSequenceImagesIfNeeded() {
        if (_sequenceItems.value.isEmpty() || _currentIndex.value >= _sequenceItems.value.size) {
            return
        }

        val currentItem = _sequenceItems.value[_currentIndex.value]

        if (currentItem.title.contains("Cuci Tangan", ignoreCase = true)) {
            Log.d("SequenceExerciseViewModel", "Fixing hand washing sequence images")

            val correctHandWashingImages = getCorrectImagePathsForTitle(currentItem.title)

            val fixedItem = SequenceExerciseItem(
                id = currentItem.id,
                title = currentItem.title,
                images = correctHandWashingImages,
                correctOrder = correctHandWashingImages.indices.associateWith { it },
                category = currentItem.category
            )

            val updatedItems = _sequenceItems.value.toMutableList()
            updatedItems[_currentIndex.value] = fixedItem
            _sequenceItems.value = updatedItems

            Log.d("SequenceExerciseViewModel", "Fixed hand washing sequence images")

            resetSelections()
        }
    }

    fun shuffleCurrentSequence() {
        if (_sequenceItems.value.isEmpty() || _currentIndex.value >= _sequenceItems.value.size) {
            Log.e("SequenceExerciseViewModel", "Cannot shuffle: No items or index out of bounds")
            return
        }

        try {
            val currentItem = _sequenceItems.value[_currentIndex.value]

            Log.d("SequenceExerciseViewModel", "BEFORE SHUFFLE:")
            Log.d("SequenceExerciseViewModel", "Images: ${currentItem.images}")
            Log.d("SequenceExerciseViewModel", "CorrectOrder: ${currentItem.correctOrder}")

            currentItem.images.forEachIndexed { index, path ->
                try {
                    val assetManager = getApplication<Application>().assets
                    val exists = assetManager.open(path).use { true }
                    Log.d("SequenceExerciseViewModel", "Image path [$index] $path exists: $exists")
                } catch (e: Exception) {
                    Log.e(
                        "SequenceExerciseViewModel",
                        "Image path [$index] $path does not exist",
                        e
                    )

                    try {
                        val simplePath = path.substringAfterLast("/")
                        val exists =
                            getApplication<Application>().assets.open(simplePath).use { true }
                        Log.d(
                            "SequenceExerciseViewModel",
                            "Simplified path $simplePath exists: $exists"
                        )
                    } catch (e2: Exception) {
                        Log.e("SequenceExerciseViewModel", "Simplified path also doesn't exist", e2)
                    }
                }
            }

            val imagePaths = currentItem.images
            val indexedPaths = imagePaths.withIndex().toList()

            val shuffledIndexedPaths = indexedPaths.shuffled()

            val shuffledPaths = shuffledIndexedPaths.map { it.value }
            val correctOrderMap = mutableMapOf<Int, Int>()

            shuffledIndexedPaths.forEachIndexed { newIndex, indexedValue ->
                val originalIndex = indexedValue.index
                correctOrderMap[newIndex] = originalIndex
            }

            val updatedItem = SequenceExerciseItem(
                id = currentItem.id,
                title = currentItem.title,
                images = shuffledPaths,
                correctOrder = correctOrderMap,
                category = currentItem.category
            )

            val updatedItems = _sequenceItems.value.toMutableList()
            updatedItems[_currentIndex.value] = updatedItem
            _sequenceItems.value = updatedItems

            resetSelections()

            Log.d("SequenceExerciseViewModel", "AFTER SHUFFLE:")
            Log.d("SequenceExerciseViewModel", "Shuffled Images: $shuffledPaths")
            Log.d("SequenceExerciseViewModel", "Correct Order Map: $correctOrderMap")
            correctOrderMap.forEach { (newPos, origPos) ->
                Log.d(
                    "SequenceExerciseViewModel",
                    "Position $newPos should contain image originally at $origPos (${imagePaths[origPos]})"
                )
            }
        } catch (e: Exception) {
            Log.e("SequenceExerciseViewModel", "Error shuffling sequence", e)
        }
    }

    fun selectImage(imageIndex: Int) {
        if (_sequenceItems.value.isEmpty() || _currentIndex.value >= _sequenceItems.value.size) {
            return
        }

        val currentItem = _sequenceItems.value[_currentIndex.value]

        if (imageIndex < 0 || imageIndex >= currentItem.images.size) {
            Log.e("SequenceExerciseViewModel", "Invalid image index: $imageIndex")
            return
        }

        val currentSelections = _selectedImages.value.toMutableList()

        val firstEmptySlot = currentSelections.indexOfFirst { it == null }

        if (currentSelections.contains(imageIndex)) {
            Log.d("SequenceExerciseViewModel", "Image $imageIndex already selected")
            return
        }

        if (firstEmptySlot != -1) {
            currentSelections[firstEmptySlot] = imageIndex
            _selectedImages.value = currentSelections

            Log.d(
                "SequenceExerciseViewModel",
                "Selected image $imageIndex (${currentItem.images[imageIndex]}) to slot $firstEmptySlot"
            )
            Log.d(
                "SequenceExerciseViewModel",
                "Current selections: ${currentSelections.joinToString()}"
            )
        }
    }

    fun clearSelection(slotIndex: Int) {
        val currentSelections = _selectedImages.value.toMutableList()
        if (slotIndex >= 0 && slotIndex < currentSelections.size) {
            currentSelections[slotIndex] = null
            _selectedImages.value = currentSelections

            Log.d("SequenceExerciseViewModel", "Menghapus pilihan dari slot $slotIndex")
            Log.d(
                "SequenceExerciseViewModel",
                "Seleksi setelah dihapus: ${currentSelections.joinToString()}"
            )
        }
    }

    fun resetSelections() {
        val currentItem =
            if (_sequenceItems.value.isNotEmpty() && _currentIndex.value < _sequenceItems.value.size) {
                _sequenceItems.value[_currentIndex.value]
            } else {
                null
            }

        val imageCount = currentItem?.images?.size ?: 4
        _selectedImages.value = List(imageCount) { null }

        Log.d("SequenceExerciseViewModel", "Semua seleksi direset, slot kosong: $imageCount")
    }

    private fun checkOrder(): Boolean {
        try {
            val currentSelections = _selectedImages.value

            if (_sequenceItems.value.isEmpty() || _currentIndex.value >= _sequenceItems.value.size) {
                return false
            }

            val currentItem = _sequenceItems.value[_currentIndex.value]

            if (currentSelections.contains(null)) {
                Log.d("SequenceExerciseViewModel", "Belum semua slot terisi")
                return false
            }

            Log.d("SequenceExerciseViewModel", "Pilihan user: ${currentSelections.joinToString()}")
            Log.d("SequenceExerciseViewModel", "Urutan benar: ${currentItem.correctOrder}")

            for (slotIndex in currentSelections.indices) {
                val selectedImageIndex = currentSelections[slotIndex] ?: continue

                if (selectedImageIndex != slotIndex) {
                    Log.d(
                        "SequenceExerciseViewModel",
                        "Urutan salah pada slot $slotIndex: $selectedImageIndex"
                    )
                    return false
                }
            }
            Log.d("SequenceExerciseViewModel", "Urutan benar!")
            return true
        } catch (e: Exception) {
            Log.e("SequenceExerciseViewModel", "Error saat memeriksa urutan", e)
            return false
        }
    }

    fun submitAnswer() {
        if (checkOrder()) {
            _score.value += 1
            Log.d("SequenceExerciseViewModel", "Jawaban benar! Skor saat ini: ${_score.value}")
        } else {
            Log.d("SequenceExerciseViewModel", "Jawaban salah!")
        }

        if (_currentIndex.value < _sequenceItems.value.size - 1) {
            _currentIndex.value += 1
            resetSelections()
            Log.d(
                "SequenceExerciseViewModel",
                "Pindah ke soal berikutnya: ${_currentIndex.value + 1}/${_sequenceItems.value.size}"
            )
        } else {
            _isExerciseCompleted.value = true

            try {
                navController.navigate("therapyResultPage/${_score.value}/${_sequenceItems.value.size}")
            } catch (e: Exception) {
                Log.e("SequenceExercise", "Error navigasi: ${e.message}", e)
            }

            updateCategoryProgress()
        }
    }

    private fun updateCategoryProgress() {
        viewModelScope.launch {
            try {
                val categoryId = when (currentCategory.value) {
                    "aktivitas_urutan" -> "aktivitas_urutan"
                    else -> currentCategory.value
                }

                repository.updateCategoryProgress(
                    categoryId = categoryId,
                    progress = score.value,
                    total = sequenceItems.value.size
                )

                val updatedCategories = _categories.value.toMutableList()
                val categoryIndex = updatedCategories.indexOfFirst {
                    it.title.lowercase().contains(currentCategory.value.lowercase())
                }

                if (categoryIndex >= 0) {
                    val currentCategory = updatedCategories[categoryIndex]

                    val newPercentage = (_score.value * 100) / sequenceItems.value.size

                    updatedCategories[categoryIndex] = currentCategory.copy(
                        progress = _score.value,
                        progressPercentage = newPercentage
                    )

                    _categories.value = updatedCategories
                    Log.d(
                        "SequenceExerciseViewModel",
                        "Updated progress for category ${currentCategory.title}: ${_score.value}/${sequenceItems.value.size} (${newPercentage}%)"
                    )
                }
            } catch (e: Exception) {
                Log.e("SequenceExerciseViewModel", "Error updating category progress", e)
            }
        }
    }

    fun resetExercise() {
        _currentIndex.value = 0
        _score.value = 0
        _isExerciseCompleted.value = false
        resetSelections()
    }

    fun logDebugInfo() {
        Log.d("SequenceExerciseViewModel", "==== DEBUG INFO ====")
        Log.d("SequenceExerciseViewModel", "Current Category: ${currentCategory.value}")
        Log.d("SequenceExerciseViewModel", "Category Count: ${categories.value.size}")
        Log.d("SequenceExerciseViewModel", "Categories: ${categories.value.map { it.title }}")
        Log.d(
            "SequenceExerciseViewModel",
            "Current Sequence Items Count: ${sequenceItems.value.size}"
        )
        Log.d("SequenceExerciseViewModel", "Current Index: ${currentIndex.value}")
        Log.d(
            "SequenceExerciseViewModel",
            "Selected Images: ${selectedImages.value.joinToString()}"
        )
        Log.d("SequenceExerciseViewModel", "Error Message: ${errorMessage.value}")

        if (sequenceItems.value.isNotEmpty() && currentIndex.value < sequenceItems.value.size) {
            val currentItem = sequenceItems.value[currentIndex.value]
            Log.d("SequenceExerciseViewModel", "Current Sequence Item: ${currentItem.title}")
            Log.d("SequenceExerciseViewModel", "Images: ${currentItem.images.joinToString()}")
            Log.d("SequenceExerciseViewModel", "Correct Order: ${currentItem.correctOrder}")
        }

        try {
            val assetManager = getApplication<Application>().assets
            Log.d("SequenceExerciseViewModel", "Root Assets:")
            assetManager.list("")?.forEach { Log.d("SequenceExerciseViewModel", "- $it") }

            if (assetManager.list("")?.contains("database") == true) {
                Log.d("SequenceExerciseViewModel", "Database Assets:")
                assetManager.list("database")?.forEach {
                    Log.d("SequenceExerciseViewModel", "- database/$it")
                }
            }

            if (assetManager.list("")?.contains("images") == true) {
                Log.d("SequenceExerciseViewModel", "Image Assets:")
                assetManager.list("images")?.forEach {
                    Log.d("SequenceExerciseViewModel", "- images/$it")
                }

                if (assetManager.list("images")?.contains("sequence") == true) {
                    Log.d("SequenceExerciseViewModel", "Sequence Images:")
                    assetManager.list("images/sequence")?.forEach {
                        Log.d("SequenceExerciseViewModel", "- images/sequence/$it")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SequenceExerciseViewModel", "Error listing assets", e)
        }
    }
}

class SequenceExerciseViewModelFactory(
    private val navController: NavController,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SequenceExerciseViewModel::class.java)) {
            return SequenceExerciseViewModel(
                navController,
                application,
                AppModule.provideFlashcardRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}