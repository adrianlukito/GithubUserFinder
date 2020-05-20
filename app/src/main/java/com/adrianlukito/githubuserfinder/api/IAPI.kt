package com.adrianlukito.githubuserfinder.api

import com.adrianlukito.githubuserfinder.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IAPI {
    @GET("/search/users")
    fun getUserList(
        @Query("q") q:String,
        @Query("page") page:Int = 1,
        @Query("per_page") perPage:Int = 30): Observable<User>
}