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
    String game;

    public Location(@NonNull String name, @NonNull String gameName) {
        if(StringUtils.isEmpty(name)) throw new InvalidInputException("location cannot be blank");
        if(StringUtils.isEmpty(gameName)) throw new InvalidInputException("gameName cannot be blank");
        this.name = name;
        this.game = gameName;
    }
}
