package com.karina.carawicara.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.SequenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SequenceDao {
    @Query("SELECT * FROM sequence")
    fun getAllSequence(): Flow<List<SequenceEntity>>

    @Query("SELECT * FROM sequence WHERE categoryId = :categoryId")
    fun getSequenceByCategory(categoryId: String): Flow<List<SequenceEntity>>

    @Query("SELECT * FROM sequence WHERE categoryId = :categoryId")
    suspend fun getSequenceByCategoryDirect(categoryId: String): List<SequenceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSequence(sequence: SequenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSequence(sequence: List<SequenceEntity>)

    @Query("SELECT COUNT(*) FROM sequence")
    suspend fun getSequenceCount(): Int

    @Query("SELECT COUNT(*) FROM sequence WHERE categoryId = :categoryId")
    suspend fun countSequenceInCategory(categoryId: String): Int

    @Query("SELECT * FROM sequence WHERE categoryId = :categoryId LIMIT 1")
    suspend fun getSampleSequence(categoryId: String): SequenceEntity?
}