package com.breno.beerstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breno.beerstore.model.Beer;
import com.breno.beerstore.model.type.BeerType;

public interface Beers extends JpaRepository<Beer, Long> {
	
	Optional<Beer> findByNameAndType(String name, BeerType type);

}
