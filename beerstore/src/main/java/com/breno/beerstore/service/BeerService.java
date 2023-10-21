package com.breno.beerstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breno.beerstore.model.Beer;
import com.breno.beerstore.repository.BeersRepository;
import com.breno.beerstore.service.exception.BeerAlreadyExistException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BeerService {
	
	@Autowired
    private BeersRepository beers;

    public BeerService(@Autowired BeersRepository beers) {
        this.beers = beers;
    }

    public Beer cadastrar(final Beer beer) {
        verifyIfBeerExists(beer);
        return beers.save(beer);
    }

    public void delete(final Long id) {
        final Optional<Beer> beerById = beers.findById(id);

        final Beer beerToDelete = beerById.orElseThrow(EntityNotFoundException::new);
        beers.delete(beerToDelete);
    }

    private void verifyIfBeerExists(final Beer beer) {
        Optional<Beer> beerByNameAndType = beers.findByNomeAndType(beer.getNome(), beer.getType());

        if (beerByNameAndType.isPresent() && (beer.isNew() || isUpdatingToADifferentBeer(beer, beerByNameAndType))) {
            throw new BeerAlreadyExistException();
        }
    }

    private boolean isUpdatingToADifferentBeer(Beer beer, Optional<Beer> beerByNameAndType) {
        return beer.alreadyExist() && !beerByNameAndType.get().equals(beer);
    }

	public List<Beer> buscarTodas() {		
		return beers.findAll();
	}

}
