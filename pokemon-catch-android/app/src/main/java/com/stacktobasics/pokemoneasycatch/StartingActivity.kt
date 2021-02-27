package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)

        var pb : ProgressBar = findViewById(R.id.progressBar)

        // ...

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val getGamesUrl = "http://172.22.240.1:8080/games"
        val getPokemonUrl = "http://172.22.240.1:8080/pokemon"
        val activityIntent = Intent(this, SelectGamesActivity::class.java)

        var noOfRequestsCompleted = 0

        // Request a string response from the provided URL.
        val gameRequest = JsonArrayRequest(
            Request.Method.GET, getGamesUrl, null,
            Response.Listener { response ->
                val type = object : TypeToken<List<Game>>() {}.type
                Store.games = Gson().fromJson(response.toString(), type)
                noOfRequestsCompleted++
            },
            Response.ErrorListener { error ->
                println("Game request failed")
                println("Game status code: " + error.networkResponse.statusCode)
            })

        val pokemonRequest = JsonArrayRequest(
            Request.Method.GET, getPokemonUrl, null,
            Response.Listener { response ->
                val type = object : TypeToken<List<Pokemon>>() {}.type
                Store.pokemon = Gson().fromJson(response.toString(), type)
                noOfRequestsCompleted++
            },
            Response.ErrorListener { error ->
                println("Pokemon request failed")
                println("Pokemon status code: " + error.networkResponse.statusCode)
            })

        // Add the request to the RequestQueue.
        queue.add(gameRequest)
        queue.add(pokemonRequest)
        queue.addRequestFinishedListener<Any> {
            if(noOfRequestsCompleted == 2) {
                pb.visibility = ProgressBar.VISIBLE
                startActivity(activityIntent)
            }
        }

    }


}
