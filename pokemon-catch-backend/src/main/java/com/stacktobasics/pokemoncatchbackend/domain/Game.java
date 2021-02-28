package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Game implements AggregateRoot {
    @Id
    String id;
    String name;

    public Game(String name) {
        this.name = name;
    }
}
