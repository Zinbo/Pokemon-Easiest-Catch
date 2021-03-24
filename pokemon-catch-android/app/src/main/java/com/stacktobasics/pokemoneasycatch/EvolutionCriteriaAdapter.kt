package com.stacktobasics.pokemoneasycatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EvolutionCriteriaAdapter(private val evolutionCriteria: List<EvolutionCriteria>) : RecyclerView.Adapter<EvolutionCriteriaAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val trigger = itemView.findViewById<TextView>(R.id.trigger)
        val criteria = itemView.findViewById<TextView>(R.id.criteria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val view = inflater.inflate(R.layout.item_evolution_criteria, parent, false)
        // Return a new holder instance
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return evolutionCriteria.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val evolutionCriterion = evolutionCriteria[position]
        // Set item views based on your views and data model
        val trigger = holder.trigger
        trigger.text = evolutionCriterion.trigger

        val criteria = holder.criteria
        val sb = StringBuilder()
        evolutionCriterion.triggerCriteria.forEach { sb.appendln("[${it.type}: ${it.value}]") }
        if(sb.isBlank()) criteria.text = "-"
        else criteria.text = sb.toString()
    }
}