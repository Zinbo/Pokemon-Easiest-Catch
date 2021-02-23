package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    String id;
    List<Game> ownedGames;
    List<Pokemon> ownedPokemon;

    public User(String id) {
        this.id = id;
    }

    //TODO: Add modify methods
}
