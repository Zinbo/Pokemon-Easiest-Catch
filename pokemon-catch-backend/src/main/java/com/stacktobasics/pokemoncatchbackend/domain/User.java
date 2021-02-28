package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
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
}
