package com.stacktobasics.pokemoncatchbackend.domain.evolution;

import lombok.NonNull;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class EvolutionCriteria {
    private final List<TriggerCriteria> triggerCriteria;
    private final String trigger;

    protected EvolutionCriteria(@NonNull List<Pair<String, String>> criteria, @NonNull String trigger) {
        this.triggerCriteria = criteria.stream().map(stringStringPair -> new TriggerCriteria(stringStringPair.getFirst(), stringStringPair.getSecond())).collect(Collectors.toList());
        this.trigger = trigger;
    }
}
