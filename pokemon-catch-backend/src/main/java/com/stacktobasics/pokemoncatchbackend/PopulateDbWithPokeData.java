package com.stacktobasics.pokemoncatchbackend;

import com.stacktobasics.pokemoncatchbackend.domain.*;
import com.stacktobasics.pokemoncatchbackend.infra.PokeApiClient;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.GamesDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        List<Game> savedGames = new ArrayList<>(gameRepository.findAll());
        GamesDTO games = client.getGames();
        games.results.stream().filter(newGame -> savedGames.stream().noneMatch(savedGame -> newGame.name.equals(savedGame.getName())))
                .forEach(game -> gameRepository.save(new Game(game.name)));
    }

    public void populatePokemon() {
        List<Pokemon> pokemon = client.getPokemon().stream()
                .map(dto -> new Pokemon(dto.id, dto.name, dto.sprites.frontDefault, dto.sprites.other.officialArtwork.frontDefault))
                .collect(Collectors.toList());

        pokemon.forEach(p ->
            client.getEncountersForPokemon(p.getPokedexNumber())
                .forEach(dto ->
                        dto.versionDetails.forEach(v ->
                                v.encounterDetails.forEach(ed -> {
                                    if(CollectionUtils.isEmpty(ed.conditionalValues)) {
                                        p.addEncounter(ed.chance, dto.locationArea.name,
                                                v.version.name, ed.method.name, "none");
                                    }
                                    else ed.conditionalValues.forEach(cv ->
                                            p.addEncounter(ed.chance, dto.locationArea.name,
                                            v.version.name, ed.method.name, cv.name));

                                }))));

        pokemon.forEach(pokemonRepository::save);
    }

}
