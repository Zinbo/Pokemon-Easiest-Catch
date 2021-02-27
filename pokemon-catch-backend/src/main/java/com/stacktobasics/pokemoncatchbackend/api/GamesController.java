package com.stacktobasics.pokemoncatchbackend.api;

import com.stacktobasics.pokemoncatchbackend.PopulateDbWithPokeData;
import com.stacktobasics.pokemoncatchbackend.domain.Game;
import com.stacktobasics.pokemoncatchbackend.domain.GameRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("games")
public class GamesController {

    private final PopulateDbWithPokeData populateDbWithPokeData;
    private final GameRepository gameRepository;

    public GamesController(PopulateDbWithPokeData populateDbWithPokeData, GameRepository gameRepository) {
        this.populateDbWithPokeData = populateDbWithPokeData;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/initialise")
    public void initialiseGames(){
        populateDbWithPokeData.populateGames();
    }

    @GetMapping
    public Iterable<Game> getGames() {
        return gameRepository.findAll();
    }

}
