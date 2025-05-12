package com.karina.carawicara

import androidx.room.TypeConverter

class BooleanConverter {
    @TypeConverter
    fun fromBoolean(value: Boolean?): Int? {
        return if (value == null) null else if (value) 1 else 0
    }

    @TypeConverter
    fun toBoolean(value: Int?): Boolean? {
        return value?.let { it == 1 }
    }
}