package com.stacktobasics.pokemoncatchbackend.domain;

import com.stacktobasics.pokemoncatchbackend.domain.exceptions.InvalidInputException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
public class Location {
    String name;
    Game game;

    public Location(@NonNull String name, @NonNull String gameName) {
        this.name = name;
        if(StringUtils.isEmpty(name)) throw new InvalidInputException("location cannot be blank");
        //TODO: check that game exists
    }
}
