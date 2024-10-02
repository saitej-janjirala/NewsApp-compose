package com.saitejajanjirala.newsapp.data.TypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saitejajanjirala.newsapp.data.models.Source

class SourceTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun sourceToString(source: Source): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun stringToSource(sourceString: String): Source {
        val type = object : TypeToken<Source>() {}.type
        return gson.fromJson(sourceString, type)
    }
}