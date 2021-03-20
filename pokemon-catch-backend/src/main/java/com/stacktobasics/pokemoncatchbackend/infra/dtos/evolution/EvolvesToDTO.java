package com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.SpeciesNavigationDTO;

public class EvolvesToDTO {
    @JsonProperty("evolution_details")
    public EvolutionDetailsDTO evolutionDetails;
    @JsonProperty("is_baby")
    public boolean isBaby;
    public SpeciesNavigationDTO species;
}
