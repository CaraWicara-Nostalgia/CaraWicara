package com.karina.carawicara.di

import android.content.Context
import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.repository.FlashcardRepository
import com.karina.carawicara.data.repository.PatientRepository
import com.karina.carawicara.data.repository.UserRepository

object AppModule {
    private lateinit var database: CaraWicaraDatabase
    private lateinit var flashcardRepository: FlashcardRepository
    private lateinit var patientRepository: PatientRepository
    private lateinit var userRepository: UserRepository

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

    fun providePatientRepository(context: Context): PatientRepository {
        if (!::patientRepository.isInitialized) {
            patientRepository = PatientRepository(provideDatabase(context))
        }
        return patientRepository
    }

    fun provideUserRepository(context: Context): UserRepository {
        if (!::userRepository.isInitialized) {
            userRepository = UserRepository(provideDatabase(context))
        }
        return userRepository
    }
}