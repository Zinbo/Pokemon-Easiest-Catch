package com.stacktobasics.pokemoncatchbackend.domain.evolution;

import lombok.NonNull;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Evolution {
    private final int from;
    private final int to;
    private final List<EvolutionCriteria> waysToEvolve = new ArrayList<>();

    public Evolution(int from, int to, @NonNull List<Pair<String, String>> criteria, @NonNull String trigger) {
        this.from = from;
        this.to = to;
        EvolutionCriteria ec = new EvolutionCriteria(criteria, trigger);
        waysToEvolve.add(ec);
    }

    protected boolean matches(int from, int to) {
        return from == this.from && to == this.to;
    }

    protected void addNewWayToEvolve(@NonNull List<Pair<String, String>> criteria, @NonNull String trigger) {
        EvolutionCriteria ec = new EvolutionCriteria(criteria, trigger);
        waysToEvolve.add(ec);
    }
}
