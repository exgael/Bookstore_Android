package com.example.bookstore_android.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookApiService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000") // Replace with your actual API URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: IBookApiService = retrofit.create(IBookApiService::class.java)
}