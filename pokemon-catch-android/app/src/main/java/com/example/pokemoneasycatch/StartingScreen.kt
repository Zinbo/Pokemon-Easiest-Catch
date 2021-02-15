package com.example.pokemoneasycatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar

class StartingScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)

        var pb : ProgressBar = findViewById(R.id.progressBar)

        pb.visibility = ProgressBar.INVISIBLE

    }


}
