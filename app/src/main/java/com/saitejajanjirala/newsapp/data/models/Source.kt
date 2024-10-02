package com.saitejajanjirala.newsapp.data.models

import com.squareup.moshi.Json

data class Source(
    @Json(name = "id")
    var id: String?,
    @Json(name = "name")
    var name: String?
)
