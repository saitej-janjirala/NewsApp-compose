package com.saitejajanjirala.newsapp.data.models

import com.squareup.moshi.Json

data class HeadLine(
    @Json(name ="articles")
    var articles: List<Article>?,
    @Json(name="status")
    var status: String?,
    @Json(name="totalResults")
    var totalResults: Int?,
)