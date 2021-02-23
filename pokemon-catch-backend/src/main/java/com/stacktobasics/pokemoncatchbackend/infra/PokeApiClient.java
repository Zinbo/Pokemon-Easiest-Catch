package com.stacktobasics.pokemoncatchbackend.infra;

import com.stacktobasics.pokemoncatchbackend.infra.dtos.EncounterDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.GameDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.PokemonDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PokeApiClient {

    public static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/";
    private final RestTemplate restTemplate;
    private final boolean shouldGetAllPokemon;

    public PokeApiClient(RestTemplate restTemplate, @Value("${feature-toggle.should-get-all-pokemon}") boolean shouldGetAllPokemon) {
        this.restTemplate = restTemplate;
        this.shouldGetAllPokemon = shouldGetAllPokemon;
    }

    public GameDTO[] getGames() {
        String url = POKEAPI_BASE_URL + "version/?limit=40";
        return restTemplate.getForObject(url, GameDTO[].class);
    }

    public List<PokemonDTO> getPokemon() {
        if(shouldGetAllPokemon) return getAllPokemon();
        return getFirst10Pokemon();
    }

    private List<PokemonDTO> getFirst10Pokemon() {
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> restTemplate.getForObject(
                        String.format("%s/pokemon/%s", POKEAPI_BASE_URL, i),
                        PokemonDTO.class))
                .collect(Collectors.toList());
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

    public EncounterDTO getEncounterForPokemon(Integer id) {
        String url = String.format("%s/%s/encounters", POKEAPI_BASE_URL, id);
        return restTemplate.getForObject(url, EncounterDTO.class);
    }
}
