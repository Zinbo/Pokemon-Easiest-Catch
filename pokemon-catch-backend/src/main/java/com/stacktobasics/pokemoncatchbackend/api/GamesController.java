package com.stacktobasics.pokemoncatchbackend.api;

import com.stacktobasics.pokemoncatchbackend.PopulateDbWithPokeData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("games")
public class GamesController {

    private final PopulateDbWithPokeData populateDbWithPokeData;

    public GamesController(PopulateDbWithPokeData populateDbWithPokeData) {
        this.populateDbWithPokeData = populateDbWithPokeData;
    }

    @PostMapping("/initialise")
    public void initialiseGames(){
        populateDbWithPokeData.populateGames();
    }

}
