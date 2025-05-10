package com.karina.carawicara

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.karina.carawicara.data.DataInitializer
import com.karina.carawicara.di.AppModule
import com.karina.carawicara.navigation.AppNavHost
import com.karina.carawicara.ui.theme.CaraWicaraTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaraWicaraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }

        initializeDatabase()
        checkAssets()
    }

    private fun initializeDatabase() {
        applicationScope.launch {
            try {
                Log.d("MainActivity", "Mulai inisialisasi database...")
                val repository = AppModule.provideFlashcardRepository(applicationContext)
                val dataInitializer = DataInitializer(applicationContext, repository)

                // Cek apakah database kosong
                val isEmpty = repository.isDatabaseEmpty()
                Log.d("MainActivity", "Database kosong: $isEmpty")

                if (isEmpty) {
                    dataInitializer.initializeDatabase()
                    // Verifikasi inisialisasi database
                    delay(500) // Beri waktu untuk operasi database
                    val categoryCount = repository.getCategoryCount()
                    val kosakataCount = repository.getKosakataCount()
                    Log.d("MainActivity", "Database setelah inisialisasi - Kategori: $categoryCount, Kosakata: $kosakataCount")
                } else {
                    Log.d("MainActivity", "Database sudah terisi")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error inisialisasi database", e)
            }
        }
    }

    private fun checkAssets() {
        try {
            // Check database JSON files
            val assetList = assets.list("")
            Log.d("MainActivity", "Asset root: ${assetList?.joinToString()}")

            if (assetList?.contains("database") == true) {
                val dbAssets = assets.list("database")
                Log.d("MainActivity", "Database assets: ${dbAssets?.joinToString()}")

                // Cek file categories.json
                if (dbAssets?.contains("categories.json") == true) {
                    val json = assets.open("database/categories.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Categories JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "categories.json tidak ditemukan!")
                }

                // Cek file kosakata.json
                if (dbAssets?.contains("kosakata.json") == true) {
                    val json = assets.open("database/kosakata.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Kosakata JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "kosakata.json tidak ditemukan!")
                }

                // Cek file pelafalan.json
                if (dbAssets?.contains("pelafalan.json") == true) {
                    val json = assets.open("database/pelafalan.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Pelafalan JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "pelafalan.json tidak ditemukan!")
                }

                // Cek file sequence.json
                if (dbAssets?.contains("sequence.json") == true) {
                    val json = assets.open("database/sequence.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Sequence JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "sequence.json tidak ditemukan!")
                }

            } else {
                Log.e("MainActivity", "Folder database tidak ditemukan dalam assets!")
            }

            // Cek folder images
            if (assetList?.contains("images") == true) {
                val imageRootAssets = assets.list("images")
                Log.d("MainActivity", "Image root assets: ${imageRootAssets?.joinToString()}")

                // Cek subfolder buah
                if (imageRootAssets?.contains("buah") == true) {
                    val buahAssets = assets.list("images/buah")
                    Log.d("MainActivity", "Buah images: ${buahAssets?.joinToString()}")
                }
            } else {
                Log.e("MainActivity", "Folder images tidak ditemukan dalam assets!")
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error checking assets", e)
        }
    }
}