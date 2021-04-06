package com.stacktobasics.pokemoncatchbackend.domain.game;

import com.stacktobasics.pokemoncatchbackend.domain.AggregateRoot;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Game implements AggregateRoot {

    public static final List<String> UNUSED_GAMES = List.of("xd", "colosseum");

    @Id
    private int id;
    private String name;

    public Game(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
