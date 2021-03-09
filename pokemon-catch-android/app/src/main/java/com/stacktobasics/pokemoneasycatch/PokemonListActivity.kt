package com.stacktobasics.pokemoneasycatch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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

        val gridview = findViewById<GridView>(R.id.gridview)
        gridview.adapter = PokemonAdapter(this, Store.pokemon)
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
            else -> super.onOptionsItemSelected(item)
        }
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

        val imageView = ImageView(context)
        Picasso.get().load(pokemon[position].imageId).into(imageView)

        val set = ConstraintSet()
        imageView.id = 100
        imageView.adjustViewBounds = true
        imageView.layoutParams = ViewGroup.LayoutParams(200, 200)
        c.addView(imageView)

        val pokemonNameTextView = TextView(context)
        pokemonNameTextView.text = pokemon[position].name
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
        set.connect(imageView.getId(), ConstraintSet.BOTTOM,
            pokemonNameTextView.getId(), ConstraintSet.TOP)

        set.applyTo(c)
        return c
    }
}
