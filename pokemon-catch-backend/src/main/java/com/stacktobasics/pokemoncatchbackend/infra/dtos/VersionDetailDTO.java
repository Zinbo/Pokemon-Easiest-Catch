package com.stacktobasics.pokemoncatchbackend.infra.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VersionDetailDTO {
    Integer maxChance;
    @JsonProperty("encounter_details")
    List<EncounterDetailsDTO> encounterDetails;
    VersionDTO version;
}
