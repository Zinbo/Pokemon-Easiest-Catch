package com.stacktobasics.pokemoncatchbackend.domain;

import org.springframework.data.repository.CrudRepository;

public interface PokemonRepository extends CrudRepository<Pokemon, Integer> {
}
