package com.saitejajanjirala.newsapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @Json(name="author")
    var author: String?,
    @Json(name = "content")
    var content: String?,
    @Json(name = "description")
    var description: String?,
    @Json(name="publishedAt")
    var publishedAt: String?,
    @Json(name = "source")
    var source: Source?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "url")
    var url: String?,
    @Json(name = "urlToImage")
    var urlToImage: String?,
    @PrimaryKey(autoGenerate = true)
    val id : Int=0
) :Serializable{

}
