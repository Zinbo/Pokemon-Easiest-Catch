package com.stacktobasics.pokemoncatchbackend;

import com.stacktobasics.pokemoncatchbackend.domain.*;
import com.stacktobasics.pokemoncatchbackend.infra.PokeApiClient;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.EncounterDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.GameDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopulateDbWithPokeData {

    private final PokeApiClient client;
    private final GameRepository gameRepository;
    private final PokemonRepository pokemonRepository;

    public PopulateDbWithPokeData(PokeApiClient client, GameRepository gameRepository, PokemonRepository pokemonRepository) {
        this.client = client;
        this.gameRepository = gameRepository;
        this.pokemonRepository = pokemonRepository;
    }


    public void populateGames() {
        List<GameDTO> games = client.getGames();
        games.forEach(game -> gameRepository.save(new Game(game.id, game.name)));

        List<Pokemon> pokemon = client.getPokemon().stream()
                .map(dto -> new Pokemon(dto.id, dto.name, dto.sprites.frontDefault))
                .collect(Collectors.toList());

        pokemon.forEach(p ->
            client.getEncountersForPokemon(p.getPokedexNumber())
                .forEach(dto ->
                        dto.versionDetails.forEach(v ->
                                p.addEncounter(v.maxChance, dto.locationArea.name, v.version.name))));

        pokemon.forEach(pokemonRepository::save);
    }

}
