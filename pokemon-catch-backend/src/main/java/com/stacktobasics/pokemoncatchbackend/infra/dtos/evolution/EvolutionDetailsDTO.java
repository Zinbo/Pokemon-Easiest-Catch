package com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvolutionDetailsDTO {
    public Integer gender;
    @JsonProperty("held_item")
    public String heldItem;
    public String item;
    @JsonProperty("known_move")
    public String knownMove;
    @JsonProperty("known_move_type")
    public String knownMoveType;
    public LocationDTO location;
    @JsonProperty("min_affection")
    public String minAffection;
    @JsonProperty("min_beauty")
    public String minBeauty;
    @JsonProperty("min_happiness")
    public String minHappiness;
    @JsonProperty("min_level")
    public String minLevel;
    @JsonProperty("need_overworld_rain")
    public boolean needsOverworldRain;
    @JsonProperty("party_species")
    public String partySpecies;
    @JsonProperty("party_type")
    public String partyType;
    @JsonProperty("relative_physical_stats")
    public String relativePhysicalStats;
    @JsonProperty("time_of_day")
    public String timeOfDay;
    @JsonProperty("trade_species")
    public String tradeSpecies;
    public TriggerDTO trigger;
    @JsonProperty("turn_upside_down")
    public boolean turnUpsideDown;
}

// https://pokeapi.co/api/v2/evolution-chain/67/
// https://pokeapi.co/api/v2/evolution-chain/213/
// https://pokeapi.co/api/v2/evolution-chain/1/

