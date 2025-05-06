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
                    Log.d("DataInitializer", "Database kosong, inisialisasi data...")

                    // Load data dengan verifikasi
                    val categories = JsonDataUtil.loadCategories(context)
                    Log.d("DataInitializer", "Loaded ${categories.size} kategori dari JSON")

                    val kosakata = JsonDataUtil.loadKosakata(context)
                    Log.d("DataInitializer", "Loaded ${kosakata.size} kosakata dari JSON")

                    val pelafalan = JsonDataUtil.loadPelafalan(context)
                    Log.d("DataInitializer", "Loaded ${pelafalan.size} pelafalan dari JSON")

                    val sequence = JsonDataUtil.loadSequence(context)

                    // Insert data dengan exception handling
                    try {
                        repository.insertAllCategories(categories)
                        Log.d("DataInitializer", "Kategori berhasil dimasukkan ke database")
                    } catch (e: Exception) {
                        Log.e("DataInitializer", "Error memasukkan kategori", e)
                    }

                    try {
                        repository.insertAllKosakata(kosakata)
                        Log.d("DataInitializer", "Kosakata berhasil dimasukkan ke database")
                    } catch (e: Exception) {
                        Log.e("DataInitializer", "Error memasukkan kosakata", e)
                    }

                    try {
                        repository.insertAllPelafalan(pelafalan)
                        repository.insertAllSequence(sequence)
                        Log.d("DataInitializer", "Pelafalan dan sequence berhasil dimasukkan")
                    } catch (e: Exception) {
                        Log.e("DataInitializer", "Error memasukkan pelafalan/sequence", e)
                    }

                    // Verifikasi data
                    val categoryCount = repository.getCategoryCount()
                    val kosakataCount = repository.getKosakataCount()
                    val pelafalanCount = repository.getPelafalanCount()
                    val sequenceCount = repository.getSequenceCount()
                    Log.d("DataInitializer", "Database setelah inisialisasi - Kategori: $categoryCount, Kosakata: $kosakataCount")
                    Log.d("DataInitializer", "Database setelah inisialisasi - Kategori: $categoryCount, Pelafalan: $pelafalanCount")
                    Log.d("DataInitializer", "Database setelah inisialisasi - Kategori: $categoryCount, Sequence: $sequenceCount")
                } else {
                    Log.d("DataInitializer", "Database sudah diinisialisasi")
                }
            } catch (e: Exception) {
                Log.e("DataInitializer", "Error inisialisasi database", e)
            }
        }
    }
}