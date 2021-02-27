package com.stacktobasics.pokemoneasycatch

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SelectGamesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_games)

        val myButton = Button(this)
        myButton.setText("Push Me")

        val ll = findViewById<View>(R.id.chipGroup) as ChipGroup
        val chip1 = Chip(this)
        chip1.setText("tap me!")
        chip1.setOnClickListener { v ->
            println("tapped!")
        }
        val lp = ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT)
        ll.addView(chip1, lp)
    }
}