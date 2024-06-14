package com.github.daniellimadev.futebolapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        if (ex.getMessage().equals("Estádio já existe.")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } else if (ex.getMessage().equals("Clube não encontrado.") || ex.getMessage().equals("Estádio não encontrado.")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
