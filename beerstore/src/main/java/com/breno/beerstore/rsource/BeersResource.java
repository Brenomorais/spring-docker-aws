package com.breno.beerstore.rsource;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beers")
public class BeersResource {
	
	@GetMapping
	public List<String> all(){
		return Arrays.asList("Heineken","Backer","Stella Artois","Bohemia","Brahma");
	}

}
