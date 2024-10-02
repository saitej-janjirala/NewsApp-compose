package com.saitejajanjirala.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.saitejajanjirala.newsapp.data.TypeConverters.SourceTypeConverter
import com.saitejajanjirala.newsapp.data.models.Article

@Database(version = 1, entities = [Article::class], exportSchema = false)
@TypeConverters(
    SourceTypeConverter::class
)
abstract class DatabaseService : RoomDatabase() {
    companion object{
      const val DB_NAME = "news_db"
    }
    abstract fun getNewsDao() : NewsDao
}