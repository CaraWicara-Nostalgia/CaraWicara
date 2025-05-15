package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.TherapyHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TherapyHistoryDao {
    @Query("SELECT * FROM therapy_histories")
    fun getAllTherapyHistories(): Flow<List<TherapyHistoryEntity>>

    @Query("SELECT * FROM therapy_histories WHERE patientId = :patientId ORDER BY date DESC")
    fun getTherapyHistoriesForPatient(patientId: String): Flow<List<TherapyHistoryEntity>>

    @Query("SELECT * FROM therapy_histories WHERE id = :historyId")
    suspend fun getTherapyHistoryById(historyId: String): TherapyHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTherapyHistory(therapyHistory: TherapyHistoryEntity)

    @Query("DELETE FROM therapy_histories WHERE id = :historyId")
    suspend fun deleteTherapyHistory(historyId: String)

    @Query("DELETE FROM therapy_histories WHERE patientId = :patientId")
    suspend fun deleteTherapyHistoriesForPatient(patientId: String)

    @Query("SELECT COUNT(*) FROM therapy_histories WHERE patientId = :patientId AND therapyType LIKE '%' || :therapyType || '%'")
    suspend fun countTherapyHistoriesByType(patientId: String, therapyType: String): Int

    @Query("SELECT AVG(progressPercentage) FROM therapy_histories WHERE patientId = :patientId AND therapyType LIKE '%' || :therapyType || '%'")
    suspend fun getAverageProgressByType(patientId: String, therapyType: String): Float?}