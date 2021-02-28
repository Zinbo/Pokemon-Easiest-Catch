package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Pokemon implements AggregateRoot {
    @Id
    private Integer pokedexNumber;
    private String name;
    private String imageId;
    private EncounterDetails encounterDetails = new EncounterDetails();
    private List<Pokemon> evolutions = new ArrayList<>();

    public Pokemon(@NonNull Integer pokedexNumber, @NonNull String name, @NonNull String imageId) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.imageId = imageId;
    }

    public void addEncounter(int catchRate, @NonNull String location, @NonNull String gameName,
                             @NonNull String method, @NonNull String condition) {
         encounterDetails.addEncounter(catchRate, location, gameName, method, condition);
    }

    public void addEvolution(Integer pokedexNumber) {
        //TODO: check that pokedexNumber is valid, get from db, then add as evolution
    }
}
