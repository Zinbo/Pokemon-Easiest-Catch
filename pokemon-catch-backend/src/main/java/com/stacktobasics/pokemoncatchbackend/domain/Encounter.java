package com.stacktobasics.pokemoncatchbackend.domain;

import com.stacktobasics.pokemoncatchbackend.domain.exceptions.InvalidInputException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Encounter {
    Integer catchRate;
    Location location;

    public Encounter(@NonNull Integer catchRate, @NonNull String location, @NonNull String gameName) {
        if(catchRate <= 0) throw new InvalidInputException("Catch rate cannot be below 0");
        this.catchRate = catchRate;
        this.location = new Location(location, gameName);
    }

    public String getLocationName() {
        return location.getName();
    }
}
