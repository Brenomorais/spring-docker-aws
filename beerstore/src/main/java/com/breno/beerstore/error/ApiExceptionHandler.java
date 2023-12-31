package com.breno.beerstore.error;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.breno.beerstore.error.ErrorResponse.ApiError;
import com.breno.beerstore.service.exception.BusinessException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// Tratmento centralizado de erro o  controller advice no contexto do Spring consegue interceptar
// as exceções lançadas pela aplicação e fazer o tratamento centralizado.

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

	private static final String NO_MESSAGE_AVAILABLE = "No message" + " available";
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	private final MessageSource apiErrorMessageSource;
	
	private ResponseEntity<ErrorResponse> createResponse(String error, HttpStatus status, Locale locale) {
		final ErrorResponse errorResponse = ErrorResponse.of(status, toApiError(error, locale));
		return ResponseEntity.status(status).body(errorResponse);
	}

	//Tramento para erros de validação
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception, Locale locale) {
		//recupera todos os erros de validação em um stream
		Stream<ObjectError> errors = exception.getBindingResult().getAllErrors().stream();
		
		//processa stream mapeando a menasgem de erro informada nas anotações de validações da classe Bear
		List<ApiError> apiErrors = errors.map(ObjectError::getDefaultMessage)
				.map(code -> toApiError(code, locale))
				.collect(toList());
		
		//retorna lista de erro processa no stream
		return ResponseEntity.badRequest()
				.body(ErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrors));
	}

	//Tratamento para formato invalido
	@ExceptionHandler({ InvalidFormatException.class, HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception, Locale locale) {
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final ApiError apiError = toApiError("generic-1", locale, exception.getValue());
		final ErrorResponse errorResponse = ErrorResponse.of(status, apiError);
		return ResponseEntity.status(status).body(errorResponse);
	}

	//Tratamento para excecoes do tipo BusinessException
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessExceptions(BusinessException exception, Locale locale) {
		final String errorCode = exception.getCode();
		final HttpStatus status = exception.getStatus();
		return createResponse(errorCode, status, locale);
	}
	
	//Tratamento para erros inesperados
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalServerError(Exception exception, Locale locale) {
		LOGGER.error("Error not expected", exception);
		return createResponse("error-1", HttpStatus.INTERNAL_SERVER_ERROR, locale);
	}

	//Recebe o codigo de erro da validação usa o objeto apiErrorMessageSource para buscar a message custom e para criar o ApiError
	private ApiError toApiError(String code, Locale locale, Object... args) {
		String message;
		try {
			message = apiErrorMessageSource.getMessage(code, args, locale);
		} catch (NoSuchMessageException e) {
			//caso message não exista no arquivo seta a mensagem NO_MESSAGE_AVAILABLE
			LOGGER.error("Couldn't find any message for {} code " + "under {} locale", code);
			message = NO_MESSAGE_AVAILABLE;
		}

		return new ApiError(code, message);
	}

}