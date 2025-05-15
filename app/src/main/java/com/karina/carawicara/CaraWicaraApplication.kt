package com.karina.carawicara

import android.app.Application
import android.os.Build
import android.util.Log
import com.karina.carawicara.di.AppModule
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CaraWicaraApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO +
    CoroutineExceptionHandler { _, throwable ->
        Log.e("Error", "Coroutine error: ${throwable.message}", throwable)
    })

    override fun onCreate() {
        super.onCreate()

        Log.d("CaraWicaraApplication", "Application started")

        try {
            initializeDatabase()
        } catch (e: Exception) {
            Log.e("CaraWicaraApplication", "Error initializing database: ${e.message}", e)
        }
    }

    private fun initializeDatabase() {
        applicationScope.launch {
            try {
                Log.d("CaraWicaraApplication", "Initializing database...")
                val database = AppModule.provideDatabase(applicationContext)

                Log.d("CaraWicaraApplication", "Database version: ${database.openHelper.readableDatabase.version}")
                Log.d("CaraWicaraApplication", "Database initialized successfully")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val patientDao = database.patientDao()
                    val patientCount = patientDao.getAllPatients().collect { patients ->
                        Log.d("CaraWicaraApplication", "Number of patients: ${patients.size}")
                    }
                }

                Log.d("CaraWicaraApplication", "Database setup completed")
            } catch (e: Exception) {
                Log.e("CaraWicaraApplication", "Error during database setup: ${e.message}", e)
            }
        }
    }
}