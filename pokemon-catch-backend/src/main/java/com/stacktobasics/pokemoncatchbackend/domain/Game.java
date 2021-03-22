package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Game implements AggregateRoot {

    public static final List<String> UNUSED_GAMES = List.of("xd", "colosseum");

    @Id
    String id;
    String name;

    public Game(String name) {
        this.name = name;
    }
}
