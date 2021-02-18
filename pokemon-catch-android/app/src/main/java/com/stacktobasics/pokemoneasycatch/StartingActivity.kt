package com.stacktobasics.pokemoneasycatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)

        var pb : ProgressBar = findViewById(R.id.progressBar)

        // ...

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.google.com"
        val activityIntent = Intent(this, SelectGamesActivity::class.java)

        // Request a string response from the provided URL.
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        // Display the first 500 characters of the response string.
                        pb.visibility = ProgressBar.INVISIBLE
                        startActivity(activityIntent)

                    },
                    Response.ErrorListener {
                        println("call failed")
                        ProgressBar.VISIBLE })

        // Add the request to the RequestQueue.
                queue.add(stringRequest)

    }


}
