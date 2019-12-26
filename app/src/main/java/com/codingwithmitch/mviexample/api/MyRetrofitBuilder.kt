package com.codingwithmitch.mviexample.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofitBuilder{

    const val BASE_URL = "https://open-api.xyz/"
    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())

    }

    val apiService by lazy {
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}