package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    String id;
    Set<Game> ownedGames = new HashSet<>();
    Set<Pokemon> ownedPokemon = new HashSet<>();

    public void addGame(@NonNull String gameName, @NonNull GameRepository gameRepository) {
        Optional<Game> foundGame = gameRepository.findById(gameName);
        foundGame.ifPresentOrElse(
                game -> ownedGames.add(game),
                () -> log.warn("trying to add game \"{}\" that does not exist", gameName));
    }

    public void addGames(@NonNull List<String> gameNames, @NonNull GameRepository gameRepository) {
        List<Game> savedGames = gameRepository.findAll();
        gameNames.stream().flatMap(gameName -> {
            Optional<Game> matchedGame = savedGames.stream().filter(savedGame -> savedGame.name.equals(gameName)).findFirst();
            return matchedGame.stream();
        }).forEach(ownedGames::add);
    }

    public void addPokemon(@NonNull Integer pokedexNumber, @NonNull PokemonRepository pokemonRepository) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(pokedexNumber);
        pokemon.ifPresent(p -> ownedPokemon.add(p));
    }

    public void removePokemon(@NonNull Integer pokedexNumber, @NonNull PokemonRepository pokemonRepository) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(pokedexNumber);
        pokemon.ifPresent(p -> ownedPokemon.remove(p));
    }
}
