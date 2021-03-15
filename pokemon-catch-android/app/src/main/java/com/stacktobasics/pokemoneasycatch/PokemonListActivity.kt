package com.stacktobasics.pokemoneasycatch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.squareup.picasso.Picasso


class PokemonListActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)


        drawPokemonGridView()
    }

    private fun drawPokemonGridView() {
        // get list of pokemon to show
        // need to remove pokemon not in owned games if hideUnobtainablePokemon is true
        // need to remove pokemon in ownedPokemon list if hideOwnedPokemon is true
        val pokemonToShow = mutableListOf<Pokemon>()
        val gridview = findViewById<GridView>(R.id.gridview)
        Store.allPokemon.forEach { pokemon ->

            var shouldHideBecauseOwned = false
            if (Store.filterOptions.hideOwnedPokemon) {
                if (Store.ownedPokemon.contains(pokemon)) shouldHideBecauseOwned = true
            }

            var shouldHideBecauseUnobtainable = false
            if (Store.filterOptions.hideUnobtainablePokemon) {
                shouldHideBecauseUnobtainable = !pokemonCanBeCaught(pokemon, Store.ownedGames)
            }

            val shouldHideBecauseCannotBeCaughtInSelectedGames = !pokemonCanBeCaught(pokemon, Store.filterOptions.selectedGames)

            // if the user has selected to hide owned pokemon and hide unobtainable pokemon, then only
            // show pokemon if it is both not owned and can be obtained
            val shouldHide = (Store.filterOptions.hideOwnedPokemon && shouldHideBecauseOwned) ||
                    (Store.filterOptions.hideUnobtainablePokemon && shouldHideBecauseUnobtainable) ||
                    shouldHideBecauseCannotBeCaughtInSelectedGames;

            if (!shouldHide) pokemonToShow.add(pokemon)
        }


        val sortedPokemon = when (Store.sort) {
            SortOptions.NAME_ASC -> pokemonToShow.sortedBy { it.name }
            SortOptions.NAME_DESC -> pokemonToShow.sortedByDescending { it.name }
            SortOptions.NUMBER_ASC -> pokemonToShow.sortedBy { it.pokedexNumber }
            SortOptions.NUMBER_DESC -> pokemonToShow.sortedByDescending { it.pokedexNumber }
            else -> pokemonToShow
        }

        gridview.adapter = PokemonAdapter(
            this,
            sortedPokemon
        ) { pokedexNumber  : Int  ->
            val activityIntent = Intent(this, DescriptionActivity::class.java)
            activityIntent.putExtra("pokedex_number", pokedexNumber)
            startActivity(activityIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_filter -> {
                val activityIntent = Intent(this, FilterActivity::class.java)
                startActivity(activityIntent)
                true
            }
            R.id.action_name_asc_dropdown -> {
                Store.sort = SortOptions.NAME_ASC
                drawPokemonGridView()
                true
            }
            R.id.action_name_desc_dropdown -> {
                Store.sort = SortOptions.NAME_DESC
                drawPokemonGridView()
                true
            }
            R.id.action_number_asc_dropdown -> {
                Store.sort = SortOptions.NUMBER_ASC
                drawPokemonGridView()
                true
            }
            R.id.action_number_desc_dropdown -> {
                Store.sort = SortOptions.NUMBER_DESC
                drawPokemonGridView()
                true
            }
            R.id.action_settings -> {
                val activityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(activityIntent)
                true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

class PokemonAdapter(val context: Context, val pokemon: List<Pokemon>, val startActivityFunc : (Int) -> Unit) : BaseAdapter() {
    // 2
    override fun getCount(): Int {
        return pokemon.size
    }

    // 3
    override fun getItemId(position: Int): Long {
        return 0
    }

    // 4
    override fun getItem(position: Int): Any? {
        return null
    }

    // 5
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val c = ConstraintLayout(context)

        val imageView = ImageView(context)
        val pokemon = pokemon[position]
        Picasso.get().load(pokemon.imageId).into(imageView)

        val set = ConstraintSet()
        imageView.id = 100
        imageView.adjustViewBounds = true
        imageView.layoutParams = ViewGroup.LayoutParams(200, 200)
        val matrix = ColorMatrix()
        matrix.setSaturation(0f) //0 means grayscale

        if(!pokemonCanBeCaught(pokemon, Store.ownedGames)) {
            val cf = ColorMatrixColorFilter(matrix)
            imageView.colorFilter = cf
        }
        c.addView(imageView)

        val pokemonNameTextView = TextView(context)
        pokemonNameTextView.text = pokemon.name
        pokemonNameTextView.id = 150
        c.addView(pokemonNameTextView)
        set.clone(c)

        set.connect(pokemonNameTextView.getId(), ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        set.connect(pokemonNameTextView.getId(), ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.connect(pokemonNameTextView.getId(), ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)

        set.connect(imageView.getId(), ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        set.connect(imageView.getId(), ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        set.connect(imageView.getId(), ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.connect(imageView.id, ConstraintSet.BOTTOM,
            pokemonNameTextView.getId(), ConstraintSet.TOP)

        c.setOnClickListener {
            startActivityFunc(pokemon.pokedexNumber)
        }

        set.applyTo(c)
        return c
    }
}

fun pokemonCanBeCaught(pokemon : Pokemon, games : List<Game>) : Boolean {
    val ownedGames: List<String> = games.map { game -> game.name }
    for (encounteredGame in pokemon.encounterDetails.encounters.map { encounter -> encounter.location.game }) {
        if (ownedGames.contains(encounteredGame)) return true
    }
    return false
}