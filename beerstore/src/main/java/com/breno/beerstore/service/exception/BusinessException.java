package com.breno.beerstore.service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BusinessException extends RuntimeException {

    private final String code;
    private final HttpStatus status;
}