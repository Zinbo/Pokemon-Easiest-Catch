package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_select_games.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SelectGamesActivity : AppCompatActivity() {

    private val usersGamesUrl = "http://172.31.80.1:8080/users/1/games"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_games)

        val ll = findViewById<View>(R.id.chipGroup) as ChipGroup
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        Store.allGames.forEach { game ->
            val chip = Chip(this)
            chip.text = game.name
            val originalColour = chip.chipBackgroundColor
            setChipClickListener(chip, originalColour)
            ll.addView(chip, lp)
        }

        val gameNames = mutableListOf<String>()
        Store.ownedGames.forEach {
            gameNames.add(it.name)
        }

        val loadingDialog = LoadingDialog(this)
        val activityIntent = Intent(this, PokemonListActivity::class.java)
        fab.setOnClickListener {
            loadingDialog.showDialog()
            saveGamesAndNavigateToNextPage(gameNames, loadingDialog, activityIntent)
        }
    }

    private fun saveGamesAndNavigateToNextPage(
        gameNames: MutableList<String>,
        loadingDialog: LoadingDialog,
        activityIntent: Intent) {
        RestClient.backendAPI.saveGamesForUser(gameNames).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Store.user = response.body()
                    loadingDialog.dismissDialog()
                    startActivity(activityIntent)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("Exception when saving owned games: " + t.message)
                throw t
            }
        })
    }

    private fun setChipClickListener(chip: Chip, originalColour: ColorStateList?) {
        chip.setOnClickListener {
            var foundGame: Game? = null;
            for (g in Store.allGames) {
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