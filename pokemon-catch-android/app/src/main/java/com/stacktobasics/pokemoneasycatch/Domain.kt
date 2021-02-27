package com.stacktobasics.pokemoneasycatch

data class Game(val name: String)

data class Pokemon(val pokedexNumber: Int, val name: String, val imageId: String,
    val encounterDetails: EncounterDetails)

data class EncounterDetails(val bestCatchRate: Int, val encounters: List<Encounter>)

data class Encounter(val catchRate: Int, val location: Location, val method: String, val condition: String)

data class Location(val name: String, val game: String)

object Store {
    var games: List<Game> = emptyList()
    var pokemon: List<Pokemon> = emptyList();
}