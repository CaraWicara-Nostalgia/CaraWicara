package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.KosakataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KosakataDao {
    @Query("SELECT * FROM kosakata")
    fun getAllKosakata(): Flow<List<KosakataEntity>>

    @Query("SELECT * FROM kosakata WHERE categoryId = :categoryId")
    fun getKosakataByCategory(categoryId: String): Flow<List<KosakataEntity>>

    @Query("SELECT * FROM kosakata WHERE categoryId = :categoryId")
    suspend fun getKosakataByCategoryDirect(categoryId: String): List<KosakataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKosakata(kosakata: KosakataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKosakata(kosakata: List<KosakataEntity>)

    @Query("SELECT COUNT(*) FROM kosakata")
    suspend fun getKosakataCount(): Int

    @Query("SELECT COUNT(*) FROM kosakata WHERE categoryId = :categoryId")
    suspend fun countKosakataInCategory(categoryId: String): Int

    @Query("SELECT * FROM kosakata WHERE categoryId = :categoryId LIMIT 1")
    suspend fun getSampleKosakata(categoryId: String): KosakataEntity?
}