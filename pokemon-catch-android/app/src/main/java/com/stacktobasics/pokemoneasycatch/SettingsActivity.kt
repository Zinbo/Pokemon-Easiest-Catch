package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_select_games.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {
    private var notSelectedColour: ColorStateList? = null
    private var selectedColour = ColorStateList.valueOf(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_games)

        setupGameChips()

        val loadingDialog = LoadingDialog(this)
        val activityIntent = Intent(this, PokemonListActivity::class.java)
        fab.setOnClickListener {
            val gameNames = mutableListOf<String>()
            Store.user.ownedGames.forEach {
                gameNames.add(it.name)
            }
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
                    val user = response.body()
                    if(user != null) Store.user = user
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

    private fun setupGameChips() {
        val tempChipForColour = Chip(this)
        notSelectedColour = tempChipForColour.chipBackgroundColor
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        Store.allGames.forEach { game ->
            val chip = Chip(this)
            if (Store.user.ownedGames.contains(game)) chip.chipBackgroundColor =
                selectedColour
            else chip.chipBackgroundColor = notSelectedColour
            chip.text = game.name
            setChipClickListener(chip)
            chipGroup.addView(chip, lp)
        }
    }

    private fun setChipClickListener(chip: Chip) {
        chip.setOnClickListener {
            var foundGame: Game? = null;
            for (g in Store.allGames) {
                if (g.name == chip.text) {
                    foundGame = g
                }
            }
            if (foundGame == null) throw IllegalArgumentException("could not find game " + chip.text)

            if (Store.user.ownedGames.contains(foundGame)) {
                Store.user.ownedGames.remove(foundGame)
                chip.chipBackgroundColor = notSelectedColour
            } else {
                Store.user.ownedGames.add(foundGame)
                chip.chipBackgroundColor = selectedColour
            }
        }
    }
}