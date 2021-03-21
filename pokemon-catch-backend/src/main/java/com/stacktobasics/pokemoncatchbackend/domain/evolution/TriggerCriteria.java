package com.stacktobasics.pokemoncatchbackend.domain.evolution;

public class TriggerCriteria {
    private String type;
    private String value;

    public TriggerCriteria(String type, String value) {
        //TODO: validate these exist
        this.type = type;
        this.value = value;
    }
}
