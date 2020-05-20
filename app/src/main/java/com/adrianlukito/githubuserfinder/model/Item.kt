package com.adrianlukito.githubuserfinder.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("login")
    @Expose
    val login: String = ""
    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String? = null
}