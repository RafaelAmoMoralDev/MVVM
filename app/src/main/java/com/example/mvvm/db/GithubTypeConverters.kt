package com.example.mvvm.db

import androidx.room.TypeConverter
import java.lang.NumberFormatException

object GithubTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(',').map {
                try {
                    it.toInt()
                } catch (nfe: NumberFormatException) {
                    println("No puede convertir el número")
                }
                null
            }.filterNotNull()
        }
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(data: List<Int>?): String? {
        return data?.joinToString(",")
    }

}