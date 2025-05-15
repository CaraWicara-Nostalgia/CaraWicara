package com.karina.carawicara.ui.screen.flashcard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProgressData(
    val completed: Int = 0,
    val total: Int = 5,
    val ratio: Float = 0f,
)

class FlashcardViewModel (
    application: Application,
    private val repository: FlashcardRepository
): AndroidViewModel(application){

    private val _pelafalanProgress = MutableStateFlow(ProgressData())
    val pelafalanProgress: StateFlow<ProgressData> = _pelafalanProgress

    private val _kosakataProgress = MutableStateFlow(ProgressData())
    val kosakataProgress: StateFlow<ProgressData> = _kosakataProgress

    private val _sequenceProgress = MutableStateFlow(ProgressData())
    val sequenceProgress: StateFlow<ProgressData> = _sequenceProgress

    fun loadAllProgress() {
        viewModelScope.launch {
            try {
                loadPelafalanProgress()
                loadKosakataProgress()
                loadSequenceProgress()
            } catch (e: Exception) {
                Log.e("FlashcardViewModel", "Error loading progress", e)
            }
        }
    }

    private suspend fun loadPelafalanProgress() {
        try {
            var totalCompleted = 0
            var totalCategories = 0

            repository.getCategoriesByType("pelafalan").collect { categories ->
                totalCategories = categories.size
                totalCompleted = categories.count { it.progressPercentage > 0 }

                val ratio = if (totalCategories > 0) {
                    totalCompleted.toFloat() / totalCategories.toFloat()
                } else 0f

                _pelafalanProgress.value = ProgressData(
                    completed = totalCompleted,
                    total = totalCategories,
                    ratio = ratio
                )
            }
        } catch (e: Exception) {
            Log.e("FlashcardViewModel", "Error loading pelafalan progress", e)
        }
    }

    private suspend fun loadKosakataProgress() {
        try {
            var totalCompleted = 0
            var totalCategories = 0

            repository.getCategoriesByType("kosakata").collect { categories ->
                totalCategories = categories.size
                totalCompleted = categories.count { it.progressPercentage > 0 }

                val ratio = if (totalCategories > 0) {
                    totalCompleted.toFloat() / totalCategories.toFloat()
                } else 0f

                _kosakataProgress.value = ProgressData(
                    completed = totalCompleted,
                    total = totalCategories,
                    ratio = ratio
                )
            }
        } catch (e: Exception) {
            Log.e("FlashcardViewModel", "Error loading kosakata progress", e)
        }
    }

    private suspend fun loadSequenceProgress() {
        try {
            var totalCompleted = 0
            var totalCategories = 0

            repository.getCategoriesByType("sequence").collect { categories ->
                totalCategories = categories.size
                totalCompleted = categories.count { it.progressPercentage > 0 }

                val ratio = if (totalCategories > 0) {
                    totalCompleted.toFloat() / totalCategories.toFloat()
                } else 0f

                _sequenceProgress.value = ProgressData(
                    completed = totalCompleted,
                    total = totalCategories,
                    ratio = ratio
                )
            }
        } catch (e: Exception) {
            Log.e("FlashcardViewModel", "Error loading sequence progress", e)
        }
    }
}

class FlashcardViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            return FlashcardViewModel(
                application,
                AppModule.provideFlashcardRepository(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}