package com.stacktobasics.pokemoncatchbackend.infra.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EncounterDTO {
    @JsonProperty("location_area")
    public LocationAreaDTO locationArea;

    @JsonProperty("version_details")
    public List<VersionDetailDTO> versionDetails;
}
