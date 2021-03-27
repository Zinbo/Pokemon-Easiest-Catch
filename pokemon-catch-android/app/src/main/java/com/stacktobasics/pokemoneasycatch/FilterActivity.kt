package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_games.*

class FilterActivity : AppCompatActivity() {

    var firstTimeLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val spinner = setupSpinner(this)
        setupSwitches()
        setupResetButton(spinner)

        fab.setOnClickListener {
            startActivity(Intent(this, PokemonListActivity::class.java))
        }
    }

    private fun setupResetButton(spinner: Spinner) {
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            // on click, reset filter settings
            Store.filterOptions.selectedGame = null
            spinner.setSelection(0)

            Store.filterOptions.hideOwnedPokemon = false
            initialiseSwitch(R.id.hideOwnedSwitch, Store.filterOptions.hideOwnedPokemon)

            Store.filterOptions.hideUnobtainablePokemon = false
            initialiseSwitch(R.id.hideUnobtainablePokemon, Store.filterOptions.hideUnobtainablePokemon)
        }
    }

    private fun initialiseSwitch(id: Int, value: Boolean) {
        val switch = findViewById<Switch>(id)
        switch.isChecked = value
    }

    private fun setupSwitches() {
        val hideOwnedSwitch = findViewById<View>(R.id.hideOwnedSwitch) as Switch
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

    private fun setupSpinner(context: Context) : Spinner {
        val allGamesPlusExtra = Store.allGames.toMutableList()
        allGamesPlusExtra.add(0, Game(ALL_GAMES_SELECTION))
        val gamesAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, allGamesPlusExtra)
        val spinner = findViewById<Spinner>(R.id.gameToShowSpinner)
        val selectedGame = Store.filterOptions.selectedGame
        spinner.adapter = gamesAdapter
        if(selectedGame is Game) {
            spinner.setSelection(Store.allGames.indexOf(selectedGame)+1)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(firstTimeLoad) {
                    firstTimeLoad = false
                    return
                }
                val game = gamesAdapter.getItem(position) as Game
                if(game.name == ALL_GAMES_SELECTION) Store.filterOptions.selectedGame = null
                else Store.filterOptions.selectedGame = game
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Store.filterOptions.selectedGame = null
            }
        }
        return spinner
    }

    companion object {
        private const val ALL_GAMES_SELECTION = "All Games"
    }
}
