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
@EqualsAndHashCode(of = "pokedexNumber")
public class Pokemon implements AggregateRoot {
    @Id
    private Integer pokedexNumber;
    private String name;
    private String imageId;
    private String officialImage;
    private EncounterDetails encounterDetails = new EncounterDetails();
    private int evolutionChainId;

    public Pokemon(@NonNull Integer pokedexNumber, @NonNull String name, @NonNull String imageId, @NonNull String officialImage) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.imageId = imageId;
        this.officialImage = officialImage;
    }

    public void addEncounter(int catchRate, @NonNull String location, @NonNull String gameName,
                             @NonNull String method, @NonNull String condition) {
         encounterDetails.addEncounter(catchRate, location, gameName, method, condition);
    }
}
