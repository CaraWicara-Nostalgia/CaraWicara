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

    private val _allFlashcards =
        MutableStateFlow<Map<String, List<SequenceExerciseItem>>>(emptyMap())
    val allFlashcards: StateFlow<Map<String, List<SequenceExerciseItem>>> = _allFlashcards

    private val _categories = MutableStateFlow<List<SequenceExerciseCategory>>(emptyList())
    val categories: StateFlow<List<SequenceExerciseCategory>> = _categories

    private val _currentCategory = MutableStateFlow<String>("")
    val currentCategory: StateFlow<String> = _currentCategory

    private val _currentFlashcards = MutableStateFlow<List<SequenceExerciseItem>>(emptyList())
    val currentFlashcards: StateFlow<List<SequenceExerciseItem>> = _currentFlashcards

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
                // Coba akses database secara direct terlebih dahulu
                val categoryCount = repository.getCategoryCount()
                Log.d("SequenceExerciseViewModel", "Jumlah kategori di database: $categoryCount")

                if (categoryCount > 0) {
                    // Database sudah berisi data
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
                    // Database kosong
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

        // Reset exercise state
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
            // Log jumlah sequence di kategori ini
            val count = repository.countSequenceInCategory(category)
            Log.d("SequenceExerciseViewModel", "Jumlah sequence dalam kategori $category: $count")

            // Jika kosong, set pesan error yang jelas
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
                        // Map database entities to UI models with careful error handling
                        val sequenceItems = entityList.mapIndexed { index, entity ->
                            try {
                                val gson = Gson()

                                // Parse image resources with robust type handling
                                val imageResourcesType = object : TypeToken<List<String>>() {}.type
                                val imageResources: List<String> =
                                    gson.fromJson(entity.imageResourcesJson, imageResourcesType)
                                Log.d(
                                    "SequenceExerciseViewModel",
                                    "Parsed image resources: $imageResources"
                                )

                                // Parse correct order with robust type handling
                                val correctOrderType = object : TypeToken<List<Int>>() {}.type
                                val correctOrderList: List<Int> =
                                    gson.fromJson(entity.correctOrderJson, correctOrderType)

                                // Convert correct order list to map
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
                                null // Skip invalid entries
                            }
                        }.filterNotNull()

                        // Take up to 5 random items
                        val limitedItems = if (sequenceItems.size > 5) {
                            sequenceItems.shuffled().take(5)
                        } else {
                            sequenceItems
                        }

                        // Set to view model and prepare for display
                        if (limitedItems.isNotEmpty()) {
                            _sequenceItems.value = limitedItems
                            Log.d(
                                "SequenceExerciseViewModel",
                                "Set ${limitedItems.size} sequence items"
                            )

                            // Prepare selection slots
                            val firstItem = limitedItems.first()
                            _selectedImages.value = List(firstItem.images.size) { null }

                            // Apply any needed fixes to image paths
                            fixAllSequenceImages()

                            // Shuffle first sequence immediately
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

        // Create different sets of dummy items based on the category
        val dummyItems = when (category) {
            "aktivitas_urutan" -> {
                // This is for general daily activities
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

        // Initialize selectedImages with nulls
        if (dummyItems.isNotEmpty()) {
            val firstItem = dummyItems.first()
            _selectedImages.value = List(firstItem.images.size) { null }
        } else {
            _selectedImages.value = List(4) { null }
        }

        // Apply any needed fixes to image paths
        fixAllSequenceImages()

        shuffleCurrentSequence()
    }

    // Helper function to create a sequence with default mapping
    private fun createDummySequence(
        id: Int,
        title: String,
        imagePaths: List<String>,
        category: String
    ): SequenceExerciseItem {
        // Create a default correct order where each image is in its original position
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
                // Default fallback
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
        val updatedItems = _sequenceItems.value.mapIndexed { index, item ->
            val correctImages = getCorrectImagePathsForTitle(item.title)

            if (item.images != correctImages) {
                Log.d("SequenceExerciseViewModel", "Fixing images for item: ${item.title}")
                Log.d("SequenceExerciseViewModel", "Original images: ${item.images}")
                Log.d("SequenceExerciseViewModel", "Correct images: $correctImages")

                // Create a new sequence item with the correct images
                item.copy(
                    images = correctImages,
                    correctOrder = correctImages.indices.associateWith { it }
                )
            } else {
                // No changes needed
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

        // Check if this is the hand washing sequence with incorrect images
        if (currentItem.title.contains("Cuci Tangan", ignoreCase = true)) {
            Log.d("SequenceExerciseViewModel", "Fixing hand washing sequence images")

            // Use the correct hand washing images
            val correctHandWashingImages = getCorrectImagePathsForTitle(currentItem.title)

            // Create a new sequence item with correct images
            val fixedItem = SequenceExerciseItem(
                id = currentItem.id,
                title = currentItem.title,
                images = correctHandWashingImages,
                correctOrder = correctHandWashingImages.indices.associateWith { it },
                category = currentItem.category
            )

            // Update the sequence items list
            val updatedItems = _sequenceItems.value.toMutableList()
            updatedItems[_currentIndex.value] = fixedItem
            _sequenceItems.value = updatedItems

            Log.d("SequenceExerciseViewModel", "Fixed hand washing sequence images")

            // Reset and shuffle
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

            // Log the current state before shuffling
            Log.d("SequenceExerciseViewModel", "BEFORE SHUFFLE:")
            Log.d("SequenceExerciseViewModel", "Images: ${currentItem.images}")
            Log.d("SequenceExerciseViewModel", "CorrectOrder: ${currentItem.correctOrder}")

            // Verify image paths exist
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

                    // Try with simplified path
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

            // Get the original images and create indexed pairs
            val imagePaths = currentItem.images
            val indexedPaths = imagePaths.withIndex().toList()

            // Shuffle the indexed paths
            val shuffledIndexedPaths = indexedPaths.shuffled()

            // Create the new paths array and correctOrder map
            val shuffledPaths = shuffledIndexedPaths.map { it.value }
            val correctOrderMap = mutableMapOf<Int, Int>()

            // For each shuffled position, store the original position
            shuffledIndexedPaths.forEachIndexed { newIndex, indexedValue ->
                val originalIndex = indexedValue.index
                correctOrderMap[newIndex] = originalIndex
            }

            // Create a new sequence item with the shuffled data
            val updatedItem = SequenceExerciseItem(
                id = currentItem.id,
                title = currentItem.title,
                images = shuffledPaths,
                correctOrder = correctOrderMap,
                category = currentItem.category
            )

            // Update the sequence items list
            val updatedItems = _sequenceItems.value.toMutableList()
            updatedItems[_currentIndex.value] = updatedItem
            _sequenceItems.value = updatedItems

            // Reset selections when shuffling
            resetSelections()

            // Log the shuffled state
            Log.d("SequenceExerciseViewModel", "AFTER SHUFFLE:")
            Log.d("SequenceExerciseViewModel", "Shuffled Images: ${shuffledPaths}")
            Log.d("SequenceExerciseViewModel", "Correct Order Map: ${correctOrderMap}")
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

        // Ensure the image index is valid
        if (imageIndex < 0 || imageIndex >= currentItem.images.size) {
            Log.e("SequenceExerciseViewModel", "Invalid image index: $imageIndex")
            return
        }

        // Get the current selections
        val currentSelections = _selectedImages.value.toMutableList()

        // Find the first empty slot
        val firstEmptySlot = currentSelections.indexOfFirst { it == null }

        // If the image is already selected, ignore
        if (currentSelections.contains(imageIndex)) {
            Log.d("SequenceExerciseViewModel", "Image $imageIndex already selected")
            return
        }

        // If there's an empty slot, place the image there
        if (firstEmptySlot != -1) {
            currentSelections[firstEmptySlot] = imageIndex
            _selectedImages.value = currentSelections

            // Log for debugging
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

            // Log untuk debugging
            Log.d("SequenceExerciseViewModel", "Menghapus pilihan dari slot $slotIndex")
            Log.d(
                "SequenceExerciseViewModel",
                "Seleksi setelah dihapus: ${currentSelections.joinToString()}"
            )
        }
    }

    fun resetSelections() {
        // Get the number of images from the current sequence item
        val currentItem =
            if (_sequenceItems.value.isNotEmpty() && _currentIndex.value < _sequenceItems.value.size) {
                _sequenceItems.value[_currentIndex.value]
            } else {
                null
            }

        val imageCount = currentItem?.images?.size ?: 4
        _selectedImages.value = List(imageCount) { null }

        // Log untuk debugging
        Log.d("SequenceExerciseViewModel", "Semua seleksi direset, slot kosong: $imageCount")
    }

    fun checkOrder(): Boolean {
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
            // Latihan selesai
            _isExerciseCompleted.value = true

            try {
                // Navigasi hanya dengan parameter yang diperlukan dan hapus ID pasien dari rute
                navController.navigate("therapyResultPage/${_score.value}/${_sequenceItems.value.size}")
            } catch (e: Exception) {
                // Tambahkan penanganan kesalahan dan logging
                Log.e("SequenceExercise", "Error navigasi: ${e.message}", e)
            }

            // Update progress untuk kategori ini
            updateCategoryProgress()
        }
    }

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

    private fun updateCategoryProgress() {
        viewModelScope.launch {
            try {
                // Find the proper category ID based on the current category value
                val categoryId = when (currentCategory.value) {
                    "aktivitas_urutan" -> "aktivitas_urutan"
                    else -> currentCategory.value // Use as-is if not matched
                }

                // Update progress di database
                repository.updateCategoryProgress(
                    categoryId = categoryId,
                    progress = score.value,
                    total = sequenceItems.value.size
                )

                // Update juga di UI
                val updatedCategories = _categories.value.toMutableList()
                val categoryIndex = updatedCategories.indexOfFirst {
                    it.title.lowercase().contains(currentCategory.value.lowercase())
                }

                if (categoryIndex >= 0) {
                    val currentCategory = updatedCategories[categoryIndex]

                    // Hitung persentase baru berdasarkan skor
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

        // Lihat item sequence saat ini
        if (sequenceItems.value.isNotEmpty() && currentIndex.value < sequenceItems.value.size) {
            val currentItem = sequenceItems.value[currentIndex.value]
            Log.d("SequenceExerciseViewModel", "Current Sequence Item: ${currentItem.title}")
            Log.d("SequenceExerciseViewModel", "Images: ${currentItem.images.joinToString()}")
            Log.d("SequenceExerciseViewModel", "Correct Order: ${currentItem.correctOrder}")
        }

        // List asset files for debugging
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

                // Check sequence folder
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

    fun debugImagePaths() {
        if (_sequenceItems.value.isEmpty() || _currentIndex.value >= _sequenceItems.value.size) {
            Log.e("DEBUG", "No current sequence item to check")
            return
        }

        val currentItem = _sequenceItems.value[_currentIndex.value]
        Log.d("DEBUG", "Current sequence: ${currentItem.title}")

        // Check if the image paths actually exist in assets
        currentItem.images.forEachIndexed { index, path ->
            try {
                val assetManager = getApplication<Application>().assets
                assetManager.open(path).use {
                    Log.d("DEBUG", "Image [$index] $path EXISTS in assets")
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "Image [$index] $path DOES NOT EXIST in assets", e)

                // Try to find a similar file
                try {
                    val dir = path.substringBeforeLast("/")
                    val files = getApplication<Application>().assets.list(dir)
                    Log.d("DEBUG", "Files in $dir: ${files?.joinToString()}")
                } catch (e2: Exception) {
                    Log.e("DEBUG", "Could not list directory", e2)
                }
            }
        }

        // List all sequence images available
        try {
            val assetManager = getApplication<Application>().assets
            if (assetManager.list("")?.contains("images") == true) {
                if (assetManager.list("images")?.contains("sequence") == true) {
                    val sequenceFiles = assetManager.list("images/sequence")
                    Log.d("DEBUG", "All sequence images: ${sequenceFiles?.joinToString()}")
                }
            }
        } catch (e: Exception) {
            Log.e("DEBUG", "Error listing sequence images", e)
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