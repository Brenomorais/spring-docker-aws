package com.breno.beerstore.service.exception;

import org.springframework.http.HttpStatus;

//exceção caso o id informado não exista retorna 404
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super("generic-2", HttpStatus.NOT_FOUND);
    }
}
