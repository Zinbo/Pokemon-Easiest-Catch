package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_select_games.*
import org.json.JSONArray

class SelectGamesActivity : AppCompatActivity() {

    private val usersGamesUrl = "http://172.19.176.1:8080/users/1/games"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_games)

        val ll = findViewById<View>(R.id.chipGroup) as ChipGroup
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        Store.games.forEach { game ->
            val chip = Chip(this)
            chip.text = game.name
            val originalColour = chip.chipBackgroundColor
            setChipClickListener(chip, originalColour)
            ll.addView(chip, lp)
        }
        val loadingDialog = LoadingDialog(this)
        val queue = HttpClient.getInstance(this.applicationContext).requestQueue
        val activityIntent = Intent(this, PokemonListActivity::class.java)

        fab.setOnClickListener {
            loadingDialog.showDialog()
            saveGames(queue);
            queue.addRequestFinishedListener<Any> {
                loadingDialog.dismissDialog()
                startActivity(activityIntent)
            }
            // call API and save owned games
            // show loading icon
        }
    }

    private fun saveGames(queue: RequestQueue) {
        val gameNames = mutableListOf<String>()
        Store.ownedGames.forEach {
            gameNames.add(it.name)
        }
        val saveGamesRequest = JsonArrayRequest(
            Request.Method.POST, usersGamesUrl, JSONArray(Gson().toJson(gameNames)),
            Response.Listener { },
            Response.ErrorListener { error ->
                println("Save games request failed")
                println("Pokemon status code: " + error.networkResponse.statusCode)
            })

        queue.add(saveGamesRequest)
    }

    private fun setChipClickListener(chip: Chip, originalColour: ColorStateList?) {
        chip.setOnClickListener {
            var foundGame: Game? = null;
            for (g in Store.games) {
                if (g.name == chip.text) {
                    foundGame = g;
                }
            }
            if (foundGame == null) throw IllegalArgumentException("could not find game " + chip.text)

            if (Store.ownedGames.contains(foundGame)) {
                Store.ownedGames.remove(foundGame)
                chip.chipBackgroundColor = originalColour
            } else {
                Store.ownedGames.add(foundGame)
                chip.chipBackgroundColor = ColorStateList.valueOf(Color.RED)
            }
        }
    }
}