package com.breno.beerstore.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.breno.beerstore.model.Beer;
import com.breno.beerstore.service.BeerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/beers")
public class BeersResource {
	
	@Autowired
	private BeerService beerService;
	
	@GetMapping
	public List<Beer> all(){
		return beerService.buscarTodas();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Beer create (@Valid @RequestBody Beer beer) {		
		return beerService.cadastrar(beer);		
	}
	
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Beer beer) {
		beer.setId(id);
		beerService.cadastrar(beer);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		beerService.delete(id);
	}

}
