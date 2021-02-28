package com.stacktobasics.pokemoncatchbackend.api;

import com.stacktobasics.pokemoncatchbackend.domain.GameRepository;
import com.stacktobasics.pokemoncatchbackend.domain.User;
import com.stacktobasics.pokemoncatchbackend.domain.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public UserController(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/{id}/games")
    public void saveGames(@PathVariable String id, @RequestBody List<String> games) {
        // TODO: Set up users properly
        if(userRepository.findAll().isEmpty()) userRepository.save(new User());

        User user = userRepository.findAll().get(0);
        user.addGames(games, gameRepository);
        userRepository.save(user);
    }

}
