package com.adrianlukito.githubuserfinder.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("total_count")
    @Expose
    val totalCount: Int = 0
    @SerializedName("incomplete_results")
    @Expose
    val incomplete_results: Boolean = false
    @SerializedName("items")
    @Expose
    val items: List<Item>? = null
}