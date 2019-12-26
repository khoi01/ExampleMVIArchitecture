package com.codingwithmitch.mviexample.api

import androidx.lifecycle.LiveData
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService{

    @GET("placeholder/blogs")
    fun getBlogPosts():List<BlogPost>
    @GET("placeholder/user/{userid")
    fun getUser(
        @Path("userId")userid:String
    ): User



}