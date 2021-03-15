package com.stacktobasics.pokemoncatchbackend.infra;

import com.stacktobasics.pokemoncatchbackend.domain.Pokemon;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.EncounterDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.GamesDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.PokemonDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PokeApiClient {

    public static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/";
    private final RestTemplate restTemplate;
    private final boolean shouldGetAllPokemon;

    public PokeApiClient(@Value("${feature-toggle.should-get-all-pokemon}") boolean shouldGetAllPokemon) {
        this.restTemplate = new RestTemplate();
        this.shouldGetAllPokemon = shouldGetAllPokemon;
    }

    public GamesDTO getGames() {
        String url = POKEAPI_BASE_URL + "version/?limit=40";
        return restTemplate.getForObject(url, GamesDTO.class);
    }

    public List<PokemonDTO> getPokemon() {
        if(shouldGetAllPokemon) return getAllPokemon();
        return getFirst10Pokemon();
    }

    private List<PokemonDTO> getFirst10Pokemon() {
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
