package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Pokemon implements AggregateRoot {
    @Id
    Integer pokedexNumber;
    String imageId;
    List<Encounter> encounters = new ArrayList<>();
    List<Pokemon> evolutions = new ArrayList<>();

    public Pokemon(@NonNull Integer pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public void addEncounter(@NonNull Integer catchRate, @NonNull String location, @NonNull String gameName) {
        if(encounters.stream().anyMatch(e -> e.getLocationName().equals(location))) {
          log.warn("Tried to add encounter to pokemon {} with existing location {}", pokedexNumber, location);
          return;
        }
        encounters.add(new Encounter(catchRate, location, gameName));
    }

    public void addEvolution(Integer pokedexNumber) {
        //TODO: check that pokedexNumber is valid, get from db, then add as evolution
    }
}
