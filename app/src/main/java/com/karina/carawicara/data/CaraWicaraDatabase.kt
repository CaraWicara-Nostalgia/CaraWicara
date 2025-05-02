package com.karina.carawicara.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karina.carawicara.data.dao.CategoryDao
import com.karina.carawicara.data.dao.KosakataDao
import com.karina.carawicara.data.dao.PelafalanDao
import com.karina.carawicara.data.dao.SequenceDao
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity

@Database(
    entities = [
        CategoryEntity::class,
        KosakataEntity::class,
        PelafalanEntity::class,
        SequenceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CaraWicaraDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun kosakataDao(): KosakataDao
    abstract fun pelafalanDao(): PelafalanDao
    abstract fun sequenceDao(): SequenceDao

    companion object {
        @Volatile
        private var INSTANCE: CaraWicaraDatabase? = null

        fun getDatabase(context: Context): CaraWicaraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CaraWicaraDatabase::class.java,
                    "carawicara_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}