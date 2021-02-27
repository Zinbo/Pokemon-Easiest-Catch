package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
public class Game implements AggregateRoot {
    @Id
    String id;
    String name;

    public Game(String name) {
        this.name = name;
    }
}
