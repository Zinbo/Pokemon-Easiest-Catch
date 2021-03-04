package com.stacktobasics.pokemoneasycatch

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpClient constructor(val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HttpClient? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HttpClient(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
}

object RestClient {
    val backendRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://172.19.176.1:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val backendAPI = backendRetrofit.create(BackendAPI::class.java)

/*    val pokeAPIRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/PokeAPI/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val pokeAPI = pokeAPIRetrofit.create(PokeAPI::class.java)*/
}