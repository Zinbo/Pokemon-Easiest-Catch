package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StartingActivity : AppCompatActivity() {

    private val getGamesUrl = "http://172.31.80.1:8080/games"
    private val getPokemonUrl = "http:/172.31.80.1:8080/pokemon"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)

        val pb : ProgressBar = findViewById(R.id.progressBar)
        val queue = HttpClient.getInstance(this.applicationContext).requestQueue
        val activityIntent = Intent(this, SelectGamesActivity::class.java)

        var noOfRequestsCompleted = 0
        getGames(queue)
        getPokemon(queue)
        queue.addRequestFinishedListener<Any> {
            noOfRequestsCompleted++
            if(noOfRequestsCompleted == 2) {
                pb.visibility = ProgressBar.VISIBLE
                startActivity(activityIntent)
            }
        }

    }

    private fun getPokemon(queue: RequestQueue) {
        val pokemonRequest = JsonArrayRequest(
            Request.Method.GET, getPokemonUrl, null,
            Response.Listener { response ->
                val type = object : TypeToken<List<Pokemon>>() {}.type
                Store.allPokemon = Gson().fromJson(response.toString(), type)
            },
            Response.ErrorListener { error ->
                println("Pokemon request failed")
                println("Pokemon status code: " + error.networkResponse.statusCode)
            })

        queue.add(pokemonRequest)
    }

    private fun getGames(queue: RequestQueue) {
        val gameRequest = JsonArrayRequest(
            Request.Method.GET, getGamesUrl, null,
            Response.Listener { response ->
                val type = object : TypeToken<List<Game>>() {}.type
                Store.allGames = Gson().fromJson(response.toString(), type)

                // TODO: need to get list of filtered games from back end when saved for user
                Store.filterOptions.selectedGames = Gson().fromJson(response.toString(), type)
            },
            Response.ErrorListener { error ->
                println("Game request failed")
                println("Game status code: " + error.networkResponse.statusCode)
            })
        queue.add(gameRequest)
    }


}
