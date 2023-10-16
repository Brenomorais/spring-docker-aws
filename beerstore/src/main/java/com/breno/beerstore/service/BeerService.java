package com.breno.beerstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breno.beerstore.model.Beer;
import com.breno.beerstore.repository.Beers;
import com.breno.beerstore.service.exception.BeerAlreadyExistException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BeerService {


    private Beers beers;

    public BeerService(@Autowired Beers beers) {
        this.beers = beers;
    }

    public Beer save(final Beer beer) {
        verifyIfBeerExists(beer);
        return beers.save(beer);
    }

    public void delete(final Long id) {
        final Optional<Beer> beerById = beers.findById(id);

        final Beer beerToDelete = beerById.orElseThrow(EntityNotFoundException::new);
        beers.delete(beerToDelete);
    }

    private void verifyIfBeerExists(final Beer beer) {
        Optional<Beer> beerByNameAndType = beers.findByNameAndType(beer.getNome(), beer.getType());

        if (beerByNameAndType.isPresent() && (beer.isNew() || isUpdatingToADifferentBeer(beer, beerByNameAndType))) {
            throw new BeerAlreadyExistException();
        }
    }

    private boolean isUpdatingToADifferentBeer(Beer beer, Optional<Beer> beerByNameAndType) {
        return beer.alreadyExist() && !beerByNameAndType.get().equals(beer);
    }

}
