package com.stacktobasics.pokemoncatchbackend.domain;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EncounterDetails {
    private int bestCatchRate = -1;
    private List<Encounter> encounters = new ArrayList<>();

    protected void addEncounter(int catchRate, @NonNull String location, @NonNull String gameName,
                                @NonNull String method, @NonNull String condition) {
        Optional<Encounter> existingEncounter =
                encounters.stream().filter(e -> e.getLocationName().equals(location) && e.getLocation().getGame().equals(gameName)
                        && e.getMethod().equals(method) && e.getCondition().equals(condition)).findFirst();
        existingEncounter.ifPresentOrElse(
                e -> {
                    e.increaseCatchRate(catchRate);
                    updateBestCatchRate(e);
                },
                () -> {
                    Encounter e = new Encounter(catchRate, location, gameName, method, condition);
                    encounters.add(e);
                    updateBestCatchRate(e);
                });
    }

    private void updateBestCatchRate(Encounter e) {
        bestCatchRate = Math.max(bestCatchRate, e.getCatchRate());
    }

    public int getBestCatchRate() {
        return bestCatchRate;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }
}
