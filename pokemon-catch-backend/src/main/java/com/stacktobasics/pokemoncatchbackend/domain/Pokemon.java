package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import static com.stacktobasics.pokemoncatchbackend.domain.Game.UNUSED_GAMES;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "pokedexNumber")
public class Pokemon implements AggregateRoot {
    @Id
    private Integer pokedexNumber;
    private String name;
    private @NonNull int generation;
    private EncounterDetails encounterDetails = new EncounterDetails();
    private int evolutionChainId;

    public Pokemon(@NonNull Integer pokedexNumber, @NonNull String name, @NonNull int generation) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.generation = generation;
    }

    public void addEncounter(int catchRate, @NonNull String location, @NonNull String gameName,
                             @NonNull String method, @NonNull String condition) {
         if(UNUSED_GAMES.contains(gameName)) return;
         encounterDetails.addEncounter(catchRate, location, gameName, method, condition);
    }
}
