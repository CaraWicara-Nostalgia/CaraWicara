package com.karina.carawicara.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity

object JsonDataUtil {

    fun loadCategories(context: Context): List<CategoryEntity> {
        val jsonString = loadJsonFromAsset(context, "database/categories.json")
            ?: return emptyList()

        val typeToken = object : TypeToken<List<CategoryEntity>>() {}.type
        return Gson().fromJson(jsonString, typeToken)
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

    private fun loadJsonFromAsset(context: Context, fileName: String): String?{
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
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

