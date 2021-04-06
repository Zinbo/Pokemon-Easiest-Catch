package com.stacktobasics.pokemoncatchbackend.infra;

import com.stacktobasics.pokemoncatchbackend.InternalException;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.*;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution.NamedResourceDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution.PokemonEvolutionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class PokeApiClient {

    public static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/";
    private final RestTemplate restTemplate;
    private final boolean shouldGetAllPokemon;
    public static final int LAST_GENERATION = 8;

    public PokeApiClient(@Value("${feature-toggle.should-get-all-pokemon}") boolean shouldGetAllPokemon) {
        this.restTemplate = new RestTemplate();
        this.shouldGetAllPokemon = shouldGetAllPokemon;
    }

    public List<GameDTO> getGames() {
        boolean done = false;
        List<GameDTO> games = new ArrayList<>();
        int i = 1;
        while(!done) {
            try {
                String url = String.format("%s/version/%s", POKEAPI_BASE_URL, i++);
                games.add(restTemplate.getForObject(url, GameDTO.class));
            } catch(HttpClientErrorException e) {
                if(e.getRawStatusCode() == 404) done = true;
                else throw e;
            }
        }
        return games;
    }

    public String getEnglishName(String url) {
        return Optional.ofNullable(getNames(url))
                .map(dto ->
                dto.names.stream()
                    .filter(n -> n.language.name.equals("en")).findFirst()
                    .map(n -> n.name).orElse(StringUtils.capitalize(dto.name)))
                .orElseThrow(() -> new InternalException("Could not get name for URL: " + url));
    }

    public NamesDTO getNames(String url) {
        return restTemplate.getForObject(url, NamesDTO.class);
    }

    public String getLocationName(String url) {
        return Optional.ofNullable(restTemplate.getForObject(url, LocationAreaDTO.class))
        .map(dto ->
                dto.names.stream()
                        .filter(n -> n.language.name.equals("en")).findFirst()
                        .flatMap(n -> StringUtils.isEmpty(n.name) ? Optional.empty() : Optional.of(n.name))
                        .orElseGet(() -> getEnglishName(dto.location.url)))
                .orElseThrow(() -> new InternalException("Could not get location name for URL: " + url));
    }

    public List<PokemonDTO> getPokemon() {
        if (shouldGetAllPokemon) return getAllPokemon();
        return getFirst50Pokemon();
    }

    public List<GenerationDTO> getGenerations() {
        List<GenerationDTO> generations = new ArrayList<>();
        for (int i = 1; i <= LAST_GENERATION; i++) {
            String url = String.format("%s/generation/%s", POKEAPI_BASE_URL, i);
            generations.add(restTemplate.getForObject(
                    url,
                    GenerationDTO.class));
        }
        return generations;
    }

    private List<PokemonDTO> getFirst50Pokemon() {
        List<PokemonDTO> pokemons = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String url = String.format("%s/pokemon/%s", POKEAPI_BASE_URL, i);
            PokemonDTO pokemon = restTemplate.getForObject(
                    url,
                    PokemonDTO.class);
            pokemons.add(pokemon);
        }
        return pokemons;
    }

    public List<PokemonEvolutionDTO> getEvolutionChains() {
        if(shouldGetAllPokemon) return getAllEvolutionChains();
        return getFirstXEvolutionChains();
    }

    private List<PokemonEvolutionDTO> getFirstXEvolutionChains() {
        List<PokemonEvolutionDTO> allEvolutionChains = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String url = String.format("%s/evolution-chain/%s", POKEAPI_BASE_URL, i);
            PokemonEvolutionDTO pokemon = restTemplate.getForObject(
                    url,
                    PokemonEvolutionDTO.class);
            allEvolutionChains.add(pokemon);
        }
        return allEvolutionChains;
    }

    private List<PokemonEvolutionDTO> getAllEvolutionChains() {
        boolean done = false;
        List<PokemonEvolutionDTO> allEvolutionChains = new ArrayList<>();
        int i = 1;
        while(!done) {
            try {
                String url = String.format("%s/evolution-chain/%s", POKEAPI_BASE_URL, i++);
                allEvolutionChains.add(restTemplate.getForObject(url, PokemonEvolutionDTO.class));
            } catch(HttpClientErrorException e) {
                if(e.getRawStatusCode() == 404) done = true;
                else throw e;
            }
        }
        return allEvolutionChains;
    }

    private List<PokemonDTO> getAllPokemon() {
        boolean done = false;
        List<PokemonDTO> allPokemon = new ArrayList<>();
        int i = 1;
        while(!done) {
            try {
                String url = String.format("%s/pokemon/%s", POKEAPI_BASE_URL, i++);
                allPokemon.add(restTemplate.getForObject(url, PokemonDTO.class));
            } catch(HttpClientErrorException e) {
                if(e.getRawStatusCode() == 404) done = true;
                else throw e;
            }
        }
        return allPokemon;
    }

    public List<EncounterDTO> getEncountersForPokemon(Integer id) {
        String url = String.format("%s/pokemon/%s/encounters", POKEAPI_BASE_URL, id);
        EncounterDTO[] dtos = restTemplate.getForObject(url, EncounterDTO[].class);
        if(dtos == null) return new ArrayList<>();
        return Arrays.asList(dtos);
    }
}
