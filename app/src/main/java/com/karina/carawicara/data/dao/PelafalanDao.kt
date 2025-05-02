package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.PelafalanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PelafalanDao {
    @Query("SELECT * FROM pelafalan")
    fun getAllPelafalan(): Flow<List<PelafalanEntity>>

    @Query("SELECT * FROM pelafalan WHERE categoryId = :categoryId")
    fun getPelafalanByCategory(categoryId: String): Flow<List<PelafalanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPelafalan(pelafalan: PelafalanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPelafalan(pelafalan: List<PelafalanEntity>)

    @Query("SELECT COUNT(*) FROM pelafalan")
    suspend fun getPelafalanCount(): Int
}