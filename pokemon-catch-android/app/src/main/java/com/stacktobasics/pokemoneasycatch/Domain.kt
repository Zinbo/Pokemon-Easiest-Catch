package com.stacktobasics.pokemoneasycatch

data class Game(val name: String)

data class Pokemon(val pokedexNumber: Int, val name: String, val imageId: String,
    val encounterDetails: EncounterDetails)

data class EncounterDetails(val bestCatchRate: Int, val encounters: List<Encounter>)

data class Encounter(val catchRate: Int, val location: Location, val method: String, val condition: String)

data class Location(val name: String, val game: String)

data class User(val id: String, val ownedGames: Set<Game>, val ownedPokemon: Set<Pokemon>)

object Store {
    var allGames: List<Game> = emptyList()
    var allPokemon: List<Pokemon> = emptyList()
    var ownedGames: MutableList<Game> = mutableListOf()
    var ownedPokemon: MutableList<Pokemon> = mutableListOf()
    var user: User? = null
    var filterOptions = FilterOptions
}

object FilterOptions {
    var selectedGames: MutableList<Game> = mutableListOf()
    var hideUnobtainablePokemon = false
    var hideOwnedPokemon = false
}