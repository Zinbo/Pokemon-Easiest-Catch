package com.stacktobasics.pokemoneasycatch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


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
                    shouldHideBecauseCannotBeCaughtInSelectedGames

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

        val rl = RelativeLayout(context)
        rl.id = Random.nextInt()
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

        val check = ImageView(context)
        check.background = context.resources.getDrawable(R.drawable.ic_check_black_24dp)
        check.id = Random.nextInt()


        rl.addView(imageView)
        rl.addView(check)
        if(!Store.ownedPokemon.contains(pokemon)) check.visibility = ImageView.INVISIBLE
        c.addView(rl)

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

        set.connect(rl.getId(), ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        set.connect(rl.getId(), ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        set.connect(rl.getId(), ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.connect(rl.id, ConstraintSet.BOTTOM,
            pokemonNameTextView.getId(), ConstraintSet.TOP)

        val value = object : View.OnTouchListener {

            private val gestureDetector = GestureDetector(context, object :
                GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    var message = "Added ${pokemon.name} to collection"
                    if(Store.ownedPokemon.contains(pokemon)) {
                        // TODO: set user id properly
                        RestClient.backendAPI.removePokemon("1", pokemon.pokedexNumber).enqueue(object :
                            Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    Store.ownedPokemon.remove(pokemon)
                                    message = "Removed ${pokemon.name} from collection"
                                    Snackbar.make(
                                        c,
                                        message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    check.visibility = ImageView.INVISIBLE
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                println("Exception when removing pokemon: " + t.message)
                                throw t
                            }
                        })

                    }
                    else {
                        // TODO: set user id properly
                        RestClient.backendAPI.addPokemon("1", pokemon.pokedexNumber).enqueue(object :
                            Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    Store.ownedPokemon.add(pokemon)
                                    message = "Added ${pokemon.name} from collection"
                                    Snackbar.make(
                                        c,
                                        message,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    check.visibility = ImageView.VISIBLE
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                println("Exception when adding pokemon: " + t.message)
                                throw t
                            }
                        })
                    }

                    return super.onDoubleTap(e)
                }

                override fun onLongPress(e: MotionEvent?) {
                    // not implemented yet
                    /*val snackbar = Snackbar.make(
                        c,
                        "Should start ",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()*/
                    return super.onLongPress(e)
                }

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    startActivityFunc(pokemon.pokedexNumber)
                    return super.onSingleTapConfirmed(e)
                }

            })

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                Log.d(
                    "TEST",
                    "Raw event: " + event?.getAction() + ", (" + event?.getRawX() + ", " + event?.getRawY() + ")"
                );
                gestureDetector.onTouchEvent(event);
                return true;
            }
        }

        c.setOnTouchListener(value)
        /*c.setOnClickListener {
            startActivityFunc(pokemon.pokedexNumber)
        }*/

        set.applyTo(c)
        return c
    }
}

fun pokemonCanBeCaught(pokemon : Pokemon, games : List<Game>) : Boolean {
    // TODO: Need a better way of handling this, need to handle evolutions
    if(games == Store.allGames) return true
    val ownedGames: List<String> = games.map { game -> game.name }
    for (encounteredGame in pokemon.encounterDetails.encounters.map { encounter -> encounter.location.game }) {
        if (ownedGames.contains(encounteredGame)) return true
    }
    return false
}