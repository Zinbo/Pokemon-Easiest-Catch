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
    Integer id;
    String name;

    public Game(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
