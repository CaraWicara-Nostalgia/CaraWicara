package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.karina.carawicara.data.entity.PatientEntity
import com.karina.carawicara.data.entity.PatientWithLanguageAbilities
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :patientId")
    suspend fun getPatientById(patientId: String): PatientEntity?

    @Transaction
    @Query("SELECT * FROM patients")
    fun getPatientsWithLanguageAbilities(): Flow<List<PatientWithLanguageAbilities>>

    @Transaction
    @Query("SELECT * FROM patients WHERE id = :patientId")
    suspend fun getPatientWithLanguageAbilities(patientId: String): PatientWithLanguageAbilities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    @Query("DELETE FROM patients WHERE id = :patientId")
    suspend fun deletePatientById(patientId: String)
}