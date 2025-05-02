package com.karina.carawicara.data

import android.content.Context
import android.util.Log
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.utils.JsonDataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataInitializer(
    private val context: Context,
    private val repository: FlashcardRepository
)  {
    suspend fun initializeDatabase() {
        withContext(Dispatchers.IO) {
            try {
                if (repository.isDatabaseEmpty()) {
                    Log.d("DataInitializer", "Database is empty, initializing data...")

                    val categories = JsonDataUtil.loadCategories(context)
                    val kosakata = JsonDataUtil.loadKosakata(context)
                    val pelafalan = JsonDataUtil.loadPelafalan(context)
                    val sequence = JsonDataUtil.loadSequence(context)

                    repository.insertAllCategories(categories)
                    repository.insertAllKosakata(kosakata)
                    repository.insertAllPelafalan(pelafalan)
                    repository.insertAllSequence(sequence)

                    Log.d("DataInitializer", "Database initialized successfully.")
                } else {
                    Log.d("DataInitializer", "Database already initialized")
                }
            } catch (e: Exception) {
                Log.e("DataInitializer", "Error initializing database", e)
            }
        }
    }
}