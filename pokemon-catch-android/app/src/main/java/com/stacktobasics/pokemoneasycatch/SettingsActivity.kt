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

class SettingsActivity : AppCompatActivity() {
    private var notSelectedColour: ColorStateList? = null
    private var selectedColour = ColorStateList.valueOf(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_games)

        setupGameChips()

        val activityIntent = Intent(this, PokemonListActivity::class.java)
        fab.setOnClickListener {
            startActivity(activityIntent)
        }
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
            if (Store.ownedGames.contains(game)) chip.chipBackgroundColor =
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

            if (Store.ownedGames.contains(foundGame)) {
                Store.ownedGames.remove(foundGame)
                chip.chipBackgroundColor = notSelectedColour
            } else {
                Store.ownedGames.add(foundGame)
                chip.chipBackgroundColor = selectedColour
            }
        }
    }
}