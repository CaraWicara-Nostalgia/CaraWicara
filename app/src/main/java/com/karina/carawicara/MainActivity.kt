package com.karina.carawicara

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaraWicaraTheme {
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

                val isEmpty = repository.isDatabaseEmpty()
                Log.d("MainActivity", "Database kosong: $isEmpty")

                if (isEmpty) {
                    dataInitializer.initializeDatabase()
                    delay(300)
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
            val assetList = assets.list("")
            Log.d("MainActivity", "Asset root: ${assetList?.joinToString()}")

            if (assetList?.contains("database") == true) {
                val dbAssets = assets.list("database")
                Log.d("MainActivity", "Database assets: ${dbAssets?.joinToString()}")

                if (dbAssets?.contains("categories.json") == true) {
                    val json = assets.open("database/categories.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Categories JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "categories.json tidak ditemukan!")
                }

                if (dbAssets?.contains("kosakata.json") == true) {
                    val json = assets.open("database/kosakata.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Kosakata JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "kosakata.json tidak ditemukan!")
                }

                if (dbAssets?.contains("konsonan_m.json") == true) {
                    val json = assets.open("database/pelafalan.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Pelafalan JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "konsonan_m.json tidak ditemukan!")
                }

                if (dbAssets?.contains("sequence.json") == true) {
                    val json = assets.open("database/sequence.json").bufferedReader().use { it.readText() }
                    Log.d("MainActivity", "Sequence JSON: ${json.take(100)}...")
                } else {
                    Log.e("MainActivity", "sequence.json tidak ditemukan!")
                }

            } else {
                Log.e("MainActivity", "Folder database tidak ditemukan dalam assets!")
            }

            if (assetList?.contains("images") == true) {
                val imageRootAssets = assets.list("images")
                Log.d("MainActivity", "Image root assets: ${imageRootAssets?.joinToString()}")

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