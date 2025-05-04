package com.karina.carawicara.data.repository

import android.util.Log
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

    suspend fun getCategoryCount(): Int {
        return database.categoryDao().getCategoryCount()
    }

    suspend fun getKosakataCount(): Int {
        return database.kosakataDao().getKosakataCount()
    }

    suspend fun getCategoryById(categoryId: String): CategoryEntity? {
        return database.categoryDao().getCategoryById(categoryId)
    }

    suspend fun countKosakataInCategory(categoryId: String): Int {
        return database.kosakataDao().countKosakataInCategory(categoryId)
    }

    suspend fun getSampleKosakata(categoryId: String): KosakataEntity? {
        return database.kosakataDao().getSampleKosakata(categoryId)
    }

    suspend fun getAllCategoriesDirectly(): List<CategoryEntity> {
        val categories = mutableListOf<CategoryEntity>()
        try {
            allCategories.collect {
                categories.addAll(it)
            }
        } catch (e: Exception) {
            Log.e("FlashcardRepository", "Error getting categories directly", e)
        }
        return categories
    }

    suspend fun getKosakataByCategoryDirectly(categoryId: String): List<KosakataEntity> {
        val kosakata = mutableListOf<KosakataEntity>()
        try {
            getKosakataByCategory(categoryId).collect {
                kosakata.addAll(it)
            }
        } catch (e: Exception) {
            Log.e("FlashcardRepository", "Error getting kosakata directly", e)
        }
        return kosakata
    }

    suspend fun getKosakataByCategoryDirect(categoryId: String): List<KosakataEntity> {
        return database.kosakataDao().getKosakataByCategoryDirect(categoryId)
    }

    // Metode untuk ekspos akses database langsung untuk debugging
    suspend fun debugFlashcards(categoryId: String): String {
        val result = StringBuilder()

        try {
            // Cek apakah kategori ada
            val category = database.categoryDao().getCategoryById(categoryId)
            result.append("Category: ${category?.title ?: "not found"}\n")

            // Cek jumlah kosakata
            val count = database.kosakataDao().countKosakataInCategory(categoryId)
            result.append("Kosakata count: $count\n")

            // Ambil beberapa kosakata langsung
            val items = database.kosakataDao().getKosakataByCategoryDirect(categoryId)
            result.append("Direct items: ${items.size}\n")

            items.take(3).forEach {
                result.append("- ${it.word}, ${it.imageRes}\n")
            }
        } catch (e: Exception) {
            result.append("Error: ${e.message}")
        }

        return result.toString()
    }
}