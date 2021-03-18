package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class DescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val pokedexNo = intent.getIntExtra("pokedex_number", 0)
        createTable(pokedexNo, this)
    }

    private fun createTable(pokedexNo : Int, context : Context) {
        val pokemon = Store.allPokemon[pokedexNo - 1]
        findViewById<TextView>(R.id.pokemonName).text = pokemon.name.capitalize()
        val imageLayout = findViewById<ConstraintLayout>(R.id.imageLayout)
        val imageView = ImageView(context)
        Picasso.get().load(pokemon.officialImage).into(imageView)
        imageView.id = 100
        imageView.adjustViewBounds = true

        val table = findViewById<TableLayout>(R.id.tableLayout)
        val encountersSortedByCatchRate =
            pokemon.encounterDetails.encounters.sortedByDescending { encounter -> encounter.catchRate }
        for (encounter in encountersSortedByCatchRate) {
            if(!Store.ownedGames.contains(Game(encounter.location.game))) continue;

            val tableRow = TableRow(context)

            val gameTextView = TextView(context)
            gameTextView.text = encounter.location.game
            tableRow.addView(gameTextView)

            val locationTextView = TextView(context)
            locationTextView.text = encounter.location.name
            tableRow.addView(locationTextView)

            val conditionTextView = TextView(context)
            conditionTextView.text = encounter.condition
            tableRow.addView(conditionTextView)

            val methodTextView = TextView(context)
            methodTextView.text = encounter.method
            tableRow.addView(methodTextView)

            val catchRateTextView = TextView(context)
            catchRateTextView.text = encounter.catchRate.toString()
            tableRow.addView(catchRateTextView)

            table.addView(tableRow)
        }
        imageLayout.addView(imageView)

    }
}
