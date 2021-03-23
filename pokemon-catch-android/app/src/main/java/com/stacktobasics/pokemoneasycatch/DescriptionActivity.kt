package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description.*

class DescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val pokedexNo = intent.getIntExtra("pokedex_number", 0)
        drawView(pokedexNo, this)
    }

    private fun drawView(pokedexNo : Int, context : Context) {
        val pokemon = Store.allPokemon[pokedexNo - 1]
        val pokemonName = findViewById<TextView>(R.id.pokemonName)
        pokemonName.text = pokemon.name.capitalize()
        val imageLayout = findViewById<ConstraintLayout>(R.id.imageLayout)
        val imageView = ImageView(context)
        Picasso.get().load(pokemon.officialImage).into(imageView)
        imageView.adjustViewBounds = true

        drawEncountersTable(pokemon, context)
        drawEvolutionSection(pokemon, (pokemonName.parent as ViewGroup), context)
        drawHatchingSection(pokemon, (pokemonName.parent as ViewGroup), context)
        imageLayout.addView(imageView)
    }

    private fun drawHatchingSection(pokemon: Pokemon, parent: ViewGroup, context: Context) {
        val evolutionChain = Store.allEvolutionChains.find { e -> e.id == pokemon.evolutionChainId }
            ?: throw RuntimeException("pokemon ${pokemon.name} has chain id which does not exist: ${pokemon.evolutionChainId}")
        val isNotBaby =
            evolutionChain.baby == null || evolutionChain.baby.pokedexNumber != pokemon.pokedexNumber
        if(isNotBaby) removeHatchingSection(parent)
        else populateHatchingSection(evolutionChain.baby?.item ?: "")
    }

    private fun populateHatchingSection(item: String) {
        val requiresItemValue = findViewById<TextView>(R.id.requiresItemValue)
        requiresItemValue.text = item
    }

    private fun removeHatchingSection(parent: ViewGroup) {
        val hatchingCriteriaLabel = findViewById<TextView>(R.id.hatchingCriteriaLabel)
        val requiresItemLabel = findViewById<TextView>(R.id.requiresItemLabel)
        val requiresItemValue = findViewById<TextView>(R.id.requiresItemValue)

        parent.removeView(hatchingCriteriaLabel)
        parent.removeView(requiresItemLabel)
        parent.removeView(requiresItemValue)

        moveEvolutionSectionUpIfExists()
    }

    private fun moveEvolutionSectionUpIfExists() {
        val evolutionCriteria = findViewById<TextView>(R.id.evolutionCriteria) ?: return
        val encountersTable = findViewById<TableLayout>(R.id.encountersTable)
        val set = ConstraintSet()
        val mConstraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout);
        set.clone(mConstraintLayout)
        set.connect(
            evolutionCriteria.id, ConstraintSet.TOP,
            encountersTable.id, ConstraintSet.BOTTOM, 16
        )
        set.applyTo(constraintLayout)
    }

    private fun drawEvolutionSection(pokemon: Pokemon, parent: ViewGroup, context: Context) {

        val evolutionChain = Store.allEvolutionChains.find { e -> e.id == pokemon.evolutionChainId }
            ?: throw RuntimeException("pokemon ${pokemon.name} has chain id which does not exist: ${pokemon.evolutionChainId}")
        val evolution = evolutionChain.evolutions.find { e -> e.to == pokemon.pokedexNumber }
        val fromPokemonId = evolution?.from
            ?: evolutionChain.baby?.pokedexNumber
        if(fromPokemonId == null) {
            removeEvolutionSection(parent)
            return
        }

        val fromPokemon = Store.allPokemon.find { p -> p.pokedexNumber == fromPokemonId }
        if(fromPokemon == null) {
            removeEvolutionSection(parent)
            return
        }

        // evolution cannot be null if it gets here
        drawEvolvesFromValue(fromPokemon)
        drawEvolutionCriteriaTable(evolution!!, context)
    }

    private fun drawEvolvesFromValue(fromPokemon: Pokemon) {
        val evolvesFromValue = findViewById<TextView>(R.id.evolvesFromValue)
        evolvesFromValue.text = fromPokemon.name
    }

    private fun drawEvolutionCriteriaTable(evolution: Evolution, context: Context) {
        val evolutionTable = findViewById<TableLayout>(R.id.evolutionTable)
        evolution.waysToEvolve.forEach { ec ->
            val tableRow = TableRow(context)

            val triggerView = TextView(context)
            triggerView.text = ec.trigger
            tableRow.addView(triggerView)

            val criteriaView = TextView(context)
            val sb = StringBuilder()
            ec.triggerCriteria.forEach { sb.appendln("[${it.type}: ${it.value}]") }
            if(sb.isBlank()) criteriaView.text = "-"
            else criteriaView.text = sb.toString()
            tableRow.addView(criteriaView)

            evolutionTable.addView(tableRow)
        }
    }

    private fun removeEvolutionSection(parent: ViewGroup) {
        val evolvesFromValue = findViewById<TextView>(R.id.evolvesFromValue)
        val evolvesFromLabel = findViewById<TextView>(R.id.evolvesFromLabel)
        val evolutionTable = findViewById<TableLayout>(R.id.evolutionTable)
        val evolutionCriteriaLabel = findViewById<TextView>(R.id.evolutionCriteria)

        parent.removeView(evolvesFromValue)
        parent.removeView(evolvesFromLabel)
        parent.removeView(evolutionTable)
        parent.removeView(evolutionCriteriaLabel)
    }

    private fun drawEncountersTable(
        pokemon: Pokemon,
        context: Context
    ) {
        val table = findViewById<TableLayout>(R.id.encountersTable)
        val encountersSortedByCatchRate =
            pokemon.encounterDetails.encounters.sortedByDescending { encounter -> encounter.catchRate }
        for (encounter in encountersSortedByCatchRate) {
            if (!Store.user.ownedGames.contains(Game(encounter.location.game))) continue;

            val tableRow = TableRow(context)

            val gameTextView = TextView(context)
            gameTextView.text = encounter.location.game
            tableRow.addView(gameTextView)

            val catchRateTextView = TextView(context)
            catchRateTextView.text = encounter.catchRate.toString()
            tableRow.addView(catchRateTextView)

            val locationTextView = TextView(context)
            locationTextView.text = encounter.location.name
            tableRow.addView(locationTextView)

            val conditionTextView = TextView(context)
            conditionTextView.text = encounter.condition
            tableRow.addView(conditionTextView)

            val methodTextView = TextView(context)
            methodTextView.text = encounter.method
            tableRow.addView(methodTextView)

            table.addView(tableRow)
        }
    }
}
