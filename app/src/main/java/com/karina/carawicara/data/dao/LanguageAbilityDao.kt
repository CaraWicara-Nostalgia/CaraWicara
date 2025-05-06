package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.LanguageAbilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageAbilityDao {
    @Query("SELECT * FROM language_abilities")
    fun getAllLanguageAbilities(): Flow<List<LanguageAbilityEntity>>

    @Query("SELECT * FROM language_abilities WHERE patientId = :patientId")
    fun getLanguageAbilitiesByPatient(patientId: String): Flow<List<LanguageAbilityEntity>>

    @Query("SELECT * FROM language_abilities WHERE patientId = :patientId")
    suspend fun getLanguageAbilitiesByPatientDirect(patientId: String): LanguageAbilityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguageAbility(languageAbility: LanguageAbilityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLanguageAbilities(languageAbilities: List<LanguageAbilityEntity>)

    @Delete
    suspend fun deleteLanguageAbility(languageAbility: LanguageAbilityEntity)

    @Query("DELETE FROM language_abilities WHERE id = :patientId")
    suspend fun deleteLanguageAbilitiesByPatient(patientId: String)
}