package com.stacktobasics.pokemoncatchbackend.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PokemonRepository extends CrudRepository<Pokemon, Integer> {

    List<Pokemon> findAll();
}
