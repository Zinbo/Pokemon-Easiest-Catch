package com.stacktobasics.pokemoneasycatch

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private val backendRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://172.31.80.1:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val backendAPI = backendRetrofit.create(BackendAPI::class.java)
}