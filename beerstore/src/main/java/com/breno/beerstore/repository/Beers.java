package com.breno.beerstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breno.beerstore.model.Beer;

public interface Beers extends JpaRepository<Beer, Long> {

}
