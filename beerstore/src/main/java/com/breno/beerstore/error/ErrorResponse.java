package com.breno.beerstore.error;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static lombok.AccessLevel.PRIVATE;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.RequiredArgsConstructor;

//Responsavel por representar a mensagem JSON de erro

@JsonAutoDetect(fieldVisibility = ANY)
@RequiredArgsConstructor(access = PRIVATE)
public class ErrorResponse {
	
	private final int statusCode;
	private final List<ApiError> errors;

	// cria um ErrorResponse a partir de uma lista de erros, que será usado nas mensagens de validação.
	static ErrorResponse of(HttpStatus status, List<ApiError> errors) {
		return new ErrorResponse(status.value(), errors);
	}

	// recebe um único objeto para quando um erro de negócio precisar ser exibido
	static ErrorResponse of(HttpStatus status, ApiError error) {
		return of(status, Collections.singletonList(error));
	}
		
	//Classe interna para representar o erro
	@JsonAutoDetect(fieldVisibility = ANY)
	@RequiredArgsConstructor
	static class ApiError {
		private final String code;
		private final String message;
	}

}
