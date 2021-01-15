package com.softserve.teachua.exception.handler;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.softserve.teachua.dto.controller.ExceptionResponse;
import com.softserve.teachua.exception.WrongAuthenticationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WrongAuthenticationException.class)
    public final ResponseEntity<Object> handleWrongEmailExceptionn(WrongAuthenticationException exception) {
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
				.status(HttpStatus.UNAUTHORIZED.value())
				.message(exception.getMessage())
				.build();
		log.warn(exception.getMessage());
        return ResponseEntity
        			.status(HttpStatus.UNAUTHORIZED)
        			.body(exceptionResponse);
    }
	
}
