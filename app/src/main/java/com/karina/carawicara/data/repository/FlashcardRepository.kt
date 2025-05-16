package com.karina.carawicara.data.repository

import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val database: CaraWicaraDatabase) {

    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>> {
        return database.categoryDao().getCategoriesByType(type)
    }

    suspend fun insertAllCategories(categories: List<CategoryEntity>) {
        database.categoryDao().insertAllCategories(categories)
    }

    suspend fun updateCategoryProgress(categoryId: String, progress: Int, total: Int) {
        val percentage = if (total > 0) (progress * 100) / total else 0
        database.categoryDao().updateProgress(categoryId, progress, percentage)
    }

    fun getKosakataByCategory(categoryId: String): Flow<List<KosakataEntity>> {
        return database.kosakataDao().getKosakataByCategory(categoryId)
    }

    suspend fun insertAllKosakata(kosakata: List<KosakataEntity>) {
        database.kosakataDao().insertAllKosakata(kosakata)
    }

    fun getPelafalanByCategory(categoryId: String): Flow<List<PelafalanEntity>> {
        return database.pelafalanDao().getPelafalanByCategory(categoryId)
    }

    suspend fun insertAllPelafalan(pelafalan: List<PelafalanEntity>) {
        database.pelafalanDao().insertAllPelafalan(pelafalan)
    }

    fun getSequenceByCategory(categoryId: String): Flow<List<SequenceEntity>> {
        return database.sequenceDao().getSequenceByCategory(categoryId)
    }

    suspend fun insertAllSequence(sequence: List<SequenceEntity>) {
        database.sequenceDao().insertAllSequence(sequence)
    }

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

    suspend fun getPelafalanCount(): Int {
        return database.pelafalanDao().getPelafalanCount()
    }

    suspend fun getSequenceCount(): Int {
        return database.sequenceDao().getSequenceCount()
    }

    suspend fun countKosakataInCategory(categoryId: String): Int {
        return database.kosakataDao().countKosakataInCategory(categoryId)
    }

    suspend fun countPelafalanInCategory(categoryId: String): Int {
        return database.pelafalanDao().countPelafalanInCategory(categoryId)
    }

    suspend fun countSequenceInCategory(categoryId: String): Int {
        return database.sequenceDao().countSequenceInCategory(categoryId)
    }
}