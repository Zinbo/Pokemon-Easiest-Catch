package com.stacktobasics.pokemoneasycatch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


class PokemonListActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)

        val gridview = findViewById<GridView>(R.id.gridview)
        gridview.adapter = PokemonAdapter(this, Store.pokemon)
    }
}

class PokemonAdapter(val context: Context, val pokemon: List<Pokemon>) : BaseAdapter() {
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
        val set = ConstraintSet()
        set.clone(c)

        val button = TextView(context)
        button.setText("Hello " + position)
        button.setId(100) // <-- Important

        c.addView(button)
        set.connect(
            button.getId(),
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        set.connect(
            button.getId(),
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            0
        )
        set.constrainHeight(button.getId(), 200)
        set.applyTo(c)


        //Button 2:


        //Button 2:
        val newButton = TextView(context)
        newButton.setText("Yeeey" + position)
        newButton.id = 150
        c.addView(newButton)
        set.connect(newButton.getId(), ConstraintSet.LEFT, button.getId(), ConstraintSet.RIGHT, 0)

        set.constrainHeight(newButton.getId(), 200)
        set.applyTo(c)

//        val lp = ConstraintLayout.LayoutParams(
//            ConstraintLayout.LayoutParams.WRAP_CONTENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        val set = ConstraintSet()
//        val dummyTextView = TextView(context)
//        dummyTextView.text = position.toString()
//        dummyTextView.id = 100
//        c.addView(dummyTextView, lp)
//
//        val pokemonNameTextView = TextView(context)
//        pokemonNameTextView.text = pokemon[position].name
//        pokemonNameTextView.id = 150
//        c.addView(pokemonNameTextView, lp)
//        set.clone(c)
//        set.connect(dummyTextView.id, ConstraintSet.TOP, pokemonNameTextView.id, ConstraintSet.BOTTOM, 60)
//        set.applyTo(c)
        return c
    }
}
