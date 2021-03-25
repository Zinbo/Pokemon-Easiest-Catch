package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)
        val selectGamesIntent = Intent(this, SelectGamesActivity::class.java)
        val pokemonListIntent = Intent(this, PokemonListActivity::class.java)

        val getData = Observable.merge(RestClient.backendAPI.getGames()
            .doOnNext { games ->
                Store.allGames = games
                Store.filterOptions.selectedGame = null
            },
            RestClient.backendAPI.getPokemon()
                .doOnNext { pokemon -> Store.allPokemon = pokemon },
            RestClient.backendAPI.getEvolutionChains()
                .doOnNext { chains -> Store.allEvolutionChains = chains },
            RestClient.backendAPI.getUser("1")
                .doOnNext { user ->
                    val fromJson = Gson().fromJson(user.charStream(), User::class.java)
                    Store.user = fromJson ?: User("1", mutableSetOf(), mutableSetOf())
                })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElements()

        val subscribe = getData.subscribe {
            if (Store.user.ownedGames.isEmpty()) startActivity(selectGamesIntent)
            else startActivity(pokemonListIntent)
        }

    }

}
