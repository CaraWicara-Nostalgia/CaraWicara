package com.karina.carawicara.di

import android.content.Context
import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.repository.FlashcardRepository

object AppModule {
    private lateinit var database: CaraWicaraDatabase
    private lateinit var flashcardRepository: FlashcardRepository

    fun provideDatabase(context: Context): CaraWicaraDatabase {
        if (!::database.isInitialized) {
            database = CaraWicaraDatabase.getDatabase(context)
        }
        return database
    }

    fun provideFlashcardRepository(context: Context): FlashcardRepository {
        if (!::flashcardRepository.isInitialized) {
            flashcardRepository = FlashcardRepository(provideDatabase(context))
        }
        return flashcardRepository
    }
}