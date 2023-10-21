package com.breno.beerstore.service.exception;

import org.springframework.http.HttpStatus;

//exceção ao tentar cadastrar uma nova cerveja já existente
public class BeerAlreadyExistException extends BusinessException  {

	public BeerAlreadyExistException() {
		super("beers-5", HttpStatus.BAD_REQUEST);
	}

}