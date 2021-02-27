package com.stacktobasics.pokemoneasycatch

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

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