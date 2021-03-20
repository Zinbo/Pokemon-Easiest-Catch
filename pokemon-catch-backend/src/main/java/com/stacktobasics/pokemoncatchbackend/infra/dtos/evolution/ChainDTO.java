package com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.SpeciesNavigationDTO;

import java.util.List;

public class ChainDTO {
    @JsonProperty("evolves_to")
    public List<EvolvesToDTO> evolvesTo;

    @JsonProperty("is_baby")
    public boolean isBaby;

    public SpeciesNavigationDTO species;
}
