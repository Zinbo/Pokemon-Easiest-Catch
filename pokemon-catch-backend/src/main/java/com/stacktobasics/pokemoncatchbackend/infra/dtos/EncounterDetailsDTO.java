package com.stacktobasics.pokemoncatchbackend.infra.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EncounterDetailsDTO {
    @JsonProperty("condition_values")
    public List<ConditionValueDTO> conditionalValues;
    public Integer chance;
    public MethodDTO method;
}
