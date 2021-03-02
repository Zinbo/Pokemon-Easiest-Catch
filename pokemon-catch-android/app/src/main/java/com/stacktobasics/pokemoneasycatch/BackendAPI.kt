package com.stacktobasics.pokemoneasycatch

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendAPI {
    @POST("users/1/games")
    fun saveGamesForUser(@Body games: List<String>) : Call<User>
}