package com.karina.carawicara.data.repository

import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val database: CaraWicaraDatabase) {

    val allCategories: Flow<List<CategoryEntity>> = database.categoryDao().getAllCategories()

    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>> {
        return database.categoryDao().getCategoriesByType(type)
    }

    suspend fun insertCategory(category: CategoryEntity) {
        database.categoryDao().insertCategory(category)
    }

    suspend fun insertAllCategories(categories: List<CategoryEntity>) {
        database.categoryDao().insertAllCategories(categories)
    }

    suspend fun updateCategoryProgress(categoryId: String, progress: Int, total: Int) {
        val percentage = if (total > 0) (progress * 100) / total else 0
        database.categoryDao().updateProgress(categoryId, progress, percentage)
    }

    // Kosakata operations
    fun getKosakataByCategory(categoryId: String): Flow<List<KosakataEntity>> {
        return database.kosakataDao().getKosakataByCategory(categoryId)
    }

    suspend fun insertAllKosakata(kosakata: List<KosakataEntity>) {
        database.kosakataDao().insertAllKosakata(kosakata)
    }

    // Pelafalan operations
    fun getPelafalanByCategory(categoryId: String): Flow<List<PelafalanEntity>> {
        return database.pelafalanDao().getPelafalanByCategory(categoryId)
    }

    suspend fun insertAllPelafalan(pelafalan: List<PelafalanEntity>) {
        database.pelafalanDao().insertAllPelafalan(pelafalan)
    }

    // Sequence operations
    fun getSequenceByCategory(categoryId: String): Flow<List<SequenceEntity>> {
        return database.sequenceDao().getSequenceByCategory(categoryId)
    }

    suspend fun insertAllSequence(sequence: List<SequenceEntity>) {
        database.sequenceDao().insertAllSequence(sequence)
    }

    // Database checking
    suspend fun isDatabaseEmpty(): Boolean {
        val categoryCount = database.categoryDao().getCategoryCount()
        return categoryCount == 0
    }
}