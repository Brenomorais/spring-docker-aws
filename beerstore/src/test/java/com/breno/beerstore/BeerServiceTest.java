package com.breno.beerstore;

import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.breno.beerstore.model.Beer;
import com.breno.beerstore.model.type.BeerType;
import com.breno.beerstore.repository.BeersRepository;
import com.breno.beerstore.service.BeerService;
import com.breno.beerstore.service.exception.BeerAlreadyExistException;

public class BeerServiceTest {

	@Mock
	private BeersRepository beersMocked;

	private BeerService beerService;

	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
		beerService = new BeerService(beersMocked);
	}

	@Test(expected = BeerAlreadyExistException.class)
	public void should_deny_creation_of_beer_that_exist() {

		final Beer beerInDatabase = new Beer(10L, "Heineken", BeerType.LAGER, new BigDecimal("355"));

		when(beersMocked.findByNomeAndType("Heineken", BeerType.LAGER)).thenReturn(Optional.of(beerInDatabase));

		final BeerService beerService = new BeerService(beersMocked);
		final Beer newBeer = new Beer(null, "Heineken", BeerType.LAGER, new BigDecimal("355"));

		beerService.cadastrar(newBeer);
	}

	@Test
	public void should_create_new_beer() {
		when(beersMocked.findByNomeAndType("Heineken", BeerType.LAGER)).thenReturn(Optional.ofNullable(null));
        
		final Beer newBeer = new Beer(null, "Heineken", BeerType.LAGER, new BigDecimal("600"));
        final Beer beerMocked = new Beer(10L, "Heineken", BeerType.LAGER, new BigDecimal("600"));
        
        when(beersMocked.save(newBeer)).thenReturn(beerMocked);
        final Beer beerSaved = beerService.cadastrar(newBeer);

	    assertThat(beerSaved.getId(), equalTo(10L));
	    assertThat(beerSaved.getNome(), equalTo("Heineken"));
	    assertThat(beerSaved.getType(), equalTo(BeerType.LAGER));
	}

	@Test
	public void should_update_beer_volume() {
		final Beer beerInDatabase = new Beer(10L, "Ultimate New", BeerType.IPA, new BigDecimal("500"));
		when(beersMocked.findByNomeAndType("Ultimate New", BeerType.IPA)).thenReturn(Optional.of(beerInDatabase));

		final Beer beerToUpdate = new Beer(10L, "Ultimate New", BeerType.IPA, new BigDecimal("200"));
		final Beer beerMocked = new Beer(10L, "Ultimate New", BeerType.IPA, new BigDecimal("200"));
		when(beersMocked.save(beerToUpdate)).thenReturn(beerMocked);

		final Beer beerUpdated = beerService.cadastrar(beerToUpdate);
		assertThat(beerUpdated.getId(), equalTo(10L));
		assertThat(beerUpdated.getNome(), equalTo("Ultimate New"));
		assertThat(beerUpdated.getType(), equalTo(BeerType.IPA));
		assertThat(beerUpdated.getVolume(), equalTo(new BigDecimal("200")));
	}

	@Test(expected = BeerAlreadyExistException.class)
	public void should_deny_update_of_an_existing_beer_that_already_exists() {
		final Beer beerInDatabase = new Beer(10L, "Heineken", BeerType.LAGER, new BigDecimal("355"));
		when(beersMocked.findByNomeAndType("Heineken", BeerType.LAGER)).thenReturn(Optional.of(beerInDatabase));

		final Beer beerToUpdate = new Beer(5L, "Heineken", BeerType.LAGER, new BigDecimal("355"));

		beerService.cadastrar(beerToUpdate);
	}

}
