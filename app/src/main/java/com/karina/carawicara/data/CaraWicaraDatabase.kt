package com.karina.carawicara.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.karina.carawicara.DateConverter
import com.karina.carawicara.data.dao.CategoryDao
import com.karina.carawicara.data.dao.KosakataDao
import com.karina.carawicara.data.dao.LanguageAbilityDao
import com.karina.carawicara.data.dao.PatientDao
import com.karina.carawicara.data.dao.PelafalanDao
import com.karina.carawicara.data.dao.SequenceDao
import com.karina.carawicara.data.dao.TherapyHistoryDao
import com.karina.carawicara.data.dao.UserDao
import com.karina.carawicara.data.entity.CategoryEntity
import com.karina.carawicara.data.entity.KosakataEntity
import com.karina.carawicara.data.entity.LanguageAbilityEntity
import com.karina.carawicara.data.entity.PatientEntity
import com.karina.carawicara.data.entity.PelafalanEntity
import com.karina.carawicara.data.entity.SequenceEntity
import com.karina.carawicara.data.entity.TherapyHistoryEntity
import com.karina.carawicara.data.entity.UserEntity

@Database(
    entities = [
        CategoryEntity::class,
        KosakataEntity::class,
        PelafalanEntity::class,
        SequenceEntity::class,
        PatientEntity::class,
        LanguageAbilityEntity::class,
        TherapyHistoryEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class CaraWicaraDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun kosakataDao(): KosakataDao
    abstract fun pelafalanDao(): PelafalanDao
    abstract fun sequenceDao(): SequenceDao
    abstract fun patientDao(): PatientDao
    abstract fun languageAbilityDao(): LanguageAbilityDao
    abstract fun therapyHistoryDao(): TherapyHistoryDao
    abstract fun userDao(): UserDao

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