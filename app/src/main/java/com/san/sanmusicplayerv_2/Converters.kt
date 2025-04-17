package com.san.sanmusicplayerv_2

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(list: List<Long>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<Long> {
        return if (data.isEmpty()) listOf() else data.split(",").map { it.toLong() }
    }
}