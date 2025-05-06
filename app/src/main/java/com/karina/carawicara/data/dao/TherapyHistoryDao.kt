package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.karina.carawicara.data.entity.TherapyHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TherapyHistoryDao {
    @Query("SELECT * FROM therapy_histories")
    fun getAllTherapyHistories(): Flow<List<TherapyHistoryEntity>>

    @Query("SELECT * FROM therapy_histories WHERE patientId = :patientId ORDER BY date DESC")
    fun getTherapyHistoriesByPatient(patientId: String): Flow<List<TherapyHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTherapyHistory(therapyHistory: TherapyHistoryEntity)

    @Update
    suspend fun updateTherapyHistory(therapyHistory: TherapyHistoryEntity)

    @Delete
    suspend fun deleteTherapyHistory(therapyHistory: TherapyHistoryEntity)

    @Query("DELETE FROM therapy_histories WHERE patientId = :patientId")
    suspend fun deleteTherapyHistoriesByPatient(patientId: String)
}