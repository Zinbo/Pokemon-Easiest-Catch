package com.stacktobasics.pokemoncatchbackend.infra.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EncounterDetailsDTO {
    @JsonProperty("conditional_values")
    List<ConditionValueDTO> conditionalValues;
    Integer chance;
    MethodDTO method;
}
