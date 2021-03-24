package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val encountersScrollView = findViewById<RecyclerView>(R.id.encountersTable)
        val set = ConstraintSet()
        val mConstraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout);
        set.clone(mConstraintLayout)
        set.connect(
            evolutionCriteria.id, ConstraintSet.TOP,
            encountersScrollView.id, ConstraintSet.BOTTOM, 16
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
        val evolutionTable = findViewById<RecyclerView>(R.id.evolutionTable)
        val adapter = EvolutionCriteriaAdapter(evolution.waysToEvolve)
        evolutionTable.adapter = adapter
        evolutionTable.layoutManager = LinearLayoutManager(this)
    }

    private fun removeEvolutionSection(parent: ViewGroup) {
        val evolvesFromValue = findViewById<TextView>(R.id.evolvesFromValue)
        val evolvesFromLabel = findViewById<TextView>(R.id.evolvesFromLabel)
        val evolutionScrollView = findViewById<RecyclerView>(R.id.evolutionTable)
        val evolutionCriteriaLabel = findViewById<TextView>(R.id.evolutionCriteria)

        parent.removeView(evolvesFromValue)
        parent.removeView(evolvesFromLabel)
        parent.removeView(evolutionScrollView)
        parent.removeView(evolutionCriteriaLabel)
    }

    private fun drawEncountersTable(
        pokemon: Pokemon,
        context: Context
    ) {
        val encountersSortedByCatchRate =
            pokemon.encounterDetails.encounters.sortedByDescending { encounter -> encounter.catchRate }

        val rvContacts = findViewById<RecyclerView>(R.id.encountersTable)
        val adapter = EncountersAdapter(encountersSortedByCatchRate)
        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(this)
    }
}
