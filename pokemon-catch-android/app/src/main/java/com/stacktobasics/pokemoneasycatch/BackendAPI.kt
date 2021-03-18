package com.stacktobasics.pokemoneasycatch

import retrofit2.Call
import retrofit2.http.*

interface BackendAPI {
    @POST("users/1/games")
    fun saveGamesForUser(@Body games: List<String>) : Call<User>

    @PUT("users/{userId}/pokemon/{pokedexNumber}")
    fun addPokemon(@Path("userId") userId: String, @Path("pokedexNumber") pokedexNumber: Int) : Call<User>

    @DELETE("users/{userId}/pokemon/{pokedexNumber}")
    fun removePokemon(@Path("userId") userId: String, @Path("pokedexNumber") pokedexNumber: Int) : Call<User>

    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: String) : Call<User>

}