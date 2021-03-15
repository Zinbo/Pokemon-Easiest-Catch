package com.stacktobasics.pokemoneasycatch

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
        findViewById<TextView>(R.id.pokemonName).text = pokemon.name
        val imageLayout = findViewById<ConstraintLayout>(R.id.imageLayout)
        val imageView = ImageView(context)
        Picasso.get().load(pokemon.imageId).into(imageView)

        val set = ConstraintSet()
        imageView.id = 100
        imageView.adjustViewBounds = true
        imageView.layoutParams = ViewGroup.LayoutParams(1000, 1000)

        imageLayout.addView(imageView)

    }
}
