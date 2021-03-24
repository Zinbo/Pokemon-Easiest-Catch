package com.stacktobasics.pokemoneasycatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EncountersAdapter(private val encounters: List<Encounter>) : RecyclerView.Adapter<EncountersAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val game = itemView.findViewById<TextView>(R.id.game)
        val catchRate = itemView.findViewById<TextView>(R.id.catchRate)
        val location = itemView.findViewById<TextView>(R.id.location)
        val condition = itemView.findViewById<TextView>(R.id.condition)
        val method = itemView.findViewById<TextView>(R.id.method)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_contact, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return encounters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val contact: Encounter = encounters[position]
        // Set item views based on your views and data model
        val game = holder.game
        game.setText(contact.location.game)

        val catchRate = holder.catchRate
        catchRate.setText(contact.catchRate.toString())

        val location = holder.location
        location.setText(contact.location.name)

        val condition = holder.condition
        condition.setText(contact.condition)

        val method = holder.method
        method.setText(contact.method)

    }


}