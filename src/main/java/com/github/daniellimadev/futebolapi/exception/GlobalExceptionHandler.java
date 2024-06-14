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
        String message = ex.getMessage();
        if (message.equals("Estádio já existe.") || message.equals("Clube já existe.") || message.equals("Os clubes da casa e visitante devem ser diferentes.") || message.equals("Um dos clubes já possui uma partida marcada com diferença menor do que 48 horas.") || message.equals("O estádio já possui uma partida cadastrada para o mesmo dia.") || message.equals("Clube inativo.") || message.equals("Data e hora da partida são anteriores à data de criação de um dos clubes.")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else if (message.equals("Clube não encontrado.") || message.equals("Estádio não encontrado.") || message.equals("Partida não encontrada.")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
