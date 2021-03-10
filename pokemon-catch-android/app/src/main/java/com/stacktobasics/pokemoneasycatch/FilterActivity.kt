package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_select_games.*

class FilterActivity : AppCompatActivity() {

    private var notSelectedColour: ColorStateList? = null
    private var selectedColour = ColorStateList.valueOf(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setupGameChips()
        setupSwitches()
        setupResetButton()

        fab.setOnClickListener {
            startActivity(Intent(this, PokemonListActivity::class.java))
        }

    }

    private fun setupResetButton() {
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            // on click, reset filter settings
            Store.filterOptions.selectedGames = Store.allGames.toMutableList()
            findViewById<ChipGroup>(R.id.chipGroup).forEach {
                val chip = it as Chip
                chip.chipBackgroundColor = selectedColour
            }

            Store.filterOptions.hideOwnedPokemon = false
            initialiseSwitch(R.id.hideOwned, Store.filterOptions.hideOwnedPokemon)

            Store.filterOptions.hideUnobtainablePokemon = false
            initialiseSwitch(R.id.hideUnobtainablePokemon, Store.filterOptions.hideUnobtainablePokemon)
        }
    }

    private fun initialiseSwitch(id: Int, value: Boolean) {
        val switch = findViewById<Switch>(id)
        switch.isChecked = value
    }

    private fun setupSwitches() {
        val hideOwnedSwitch = findViewById<View>(R.id.hideOwned) as Switch
        hideOwnedSwitch.isChecked = Store.filterOptions.hideOwnedPokemon
        hideOwnedSwitch.setOnClickListener {
            Store.filterOptions.hideOwnedPokemon = hideOwnedSwitch.isChecked
        }

        val hideUnobtainableSwitch = findViewById<View>(R.id.hideUnobtainablePokemon) as Switch
        hideUnobtainableSwitch.isChecked = Store.filterOptions.hideUnobtainablePokemon
        hideUnobtainableSwitch.setOnClickListener {
            Store.filterOptions.hideUnobtainablePokemon = hideUnobtainableSwitch.isChecked
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
            if (Store.filterOptions.selectedGames.contains(game)) chip.chipBackgroundColor =
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

            if (Store.filterOptions.selectedGames.contains(foundGame)) {
                Store.filterOptions.selectedGames.remove(foundGame)
                chip.chipBackgroundColor = notSelectedColour
            } else {
                Store.filterOptions.selectedGames.add(foundGame)
                chip.chipBackgroundColor = selectedColour
            }
        }
    }
}
