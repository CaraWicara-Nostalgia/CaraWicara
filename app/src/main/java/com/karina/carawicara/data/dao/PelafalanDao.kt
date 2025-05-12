package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.PelafalanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PelafalanDao {
    @Query("SELECT * FROM konsonan_m")
    fun getAllPelafalan(): Flow<List<PelafalanEntity>>

    @Query("SELECT * FROM konsonan_m WHERE categoryId = :categoryId")
    fun getPelafalanByCategory(categoryId: String): Flow<List<PelafalanEntity>>

    @Query("SELECT * FROM konsonan_m WHERE categoryId = :categoryId")
    suspend fun getPelafalanByCategoryDirect(categoryId: String): List<PelafalanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPelafalan(pelafalan: PelafalanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPelafalan(pelafalan: List<PelafalanEntity>)

    @Query("SELECT COUNT(*) FROM konsonan_m")
    suspend fun getPelafalanCount(): Int

    @Query("SELECT COUNT(*) FROM konsonan_m WHERE categoryId = :categoryId")
    suspend fun countPelafalanInCategory(categoryId: String): Int

    @Query("SELECT * FROM konsonan_m WHERE categoryId = :categoryId LIMIT 1")
    suspend fun getSamplePelafalan(categoryId: String): PelafalanEntity?
}