package com.stacktobasics.pokemoncatchbackend.domain;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {

}
