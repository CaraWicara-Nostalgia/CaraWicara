package com.karina.carawicara.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity

object JsonDataUtil {

    fun loadCategories(context: Context): List<CategoryEntity> {
        val jsonString = loadJsonFromAsset(context, "database/categories.json")

        if (jsonString == null) {
            Log.e("JsonDataUtil", "Failed to load categories.json from assets")
            return emptyList()
        }

        Log.d("JsonDataUtil", "Categories JSON loaded successfully, length: ${jsonString.length}")

        try {
            val typeToken = object : TypeToken<List<CategoryEntity>>() {}.type
            val categories: List<CategoryEntity> = Gson().fromJson(jsonString, typeToken)
            Log.d("JsonDataUtil", "Parsed ${categories.size} categories from JSON")

            // Log sample data
            if (categories.isNotEmpty()) {
                val sample = categories.first()
                Log.d("JsonDataUtil", "Sample category: ${sample.id}, ${sample.title}, ${sample.type}")
            }

            return categories
        } catch (e: Exception) {
            Log.e("JsonDataUtil", "Error parsing categories JSON", e)
            return emptyList()
        }
    }

    fun loadKosakata(context: Context): List<KosakataEntity> {
        val jsonString = loadJsonFromAsset(context, "database/kosakata.json")
            ?: return emptyList()

        val typeToken = object : TypeToken<List<KosakataJson>>() {}.type
        val kosakataList: List<KosakataJson> = Gson().fromJson(jsonString, typeToken)

        return kosakataList.map { json ->
            KosakataEntity(
                word = json.word,
                pronunciation = json.pronunciation,
                imageRes = json.imageRes,
                categoryId = json.category
            )
        }
    }

    fun loadPelafalan(context: Context): List<PelafalanEntity> {
        val jsonString = loadJsonFromAsset(context, "database/pelafalan.json")
            ?: return emptyList()

        val typeToken = object : TypeToken<List<PelafalanJson>>() {}.type
        val pelafalanList: List<PelafalanJson> = Gson().fromJson(jsonString, typeToken)

        return pelafalanList.map { json ->
            PelafalanEntity(
                word = json.word,
                pronunciation = json.pronunciation,
                imageRes = json.imageRes,
                categoryId = json.category
            )
        }
    }

    fun loadSequence(context: Context): List<SequenceEntity> {
        val jsonString = loadJsonFromAsset(context, "database/sequence.json")
            ?: return emptyList()

        val typeToken = object : TypeToken<List<SequenceJson>>() {}.type
        val sequenceList: List<SequenceJson> = Gson().fromJson(jsonString, typeToken)

        return sequenceList.map { json ->
            SequenceEntity(
                title = json.title,
                correctOrderJson = Gson().toJson(json.correctOrder),
                imageResourcesJson = Gson().toJson(json.imageResources),
                categoryId = json.category
            )
        }
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            Log.d("JsonDataUtil", "Successfully read $fileName, size: $size bytes")
            json
        } catch (e: Exception) {
            Log.e("JsonDataUtil", "Failed to read $fileName from assets", e)
            null
        }
    }

    data class KosakataJson(
        val word: String,
        val pronunciation: String,
        val imageRes: String,
        val category: String
    )

    data class PelafalanJson(
        val word: String,
        val pronunciation: String,
        val imageRes: String,
        val category: String
    )

    data class SequenceJson(
        val title: String,
        val correctOrder: List<String>,
        val imageResources: List<String>,
        val category: String
    )
}

