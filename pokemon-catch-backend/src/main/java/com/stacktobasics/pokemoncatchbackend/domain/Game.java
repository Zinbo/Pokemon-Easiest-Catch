package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Game implements AggregateRoot {
    @Id
    String id;
    String name;

    public Game(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
