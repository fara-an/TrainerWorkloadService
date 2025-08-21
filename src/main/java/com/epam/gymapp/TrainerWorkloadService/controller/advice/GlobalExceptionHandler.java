package com.epam.gymapp.TrainerWorkloadService.controller.advice;


import com.epam.gymapp.TrainerWorkloadService.dto.MessageResponse;

import com.epam.gymapp.TrainerWorkloadService.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE ="Error occurred during database interaction.";
    private static final String GENERAL_EXCEPTION_MESSAGE ="Something went wrong on our side. Please try again later.";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(build(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining(";"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(build(message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParamException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build("Request parameter " + ex.getParameterName() + "is missing in the  Url"));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handlePathVariableException(MissingPathVariableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(build("Request parameter " + ex.getVariableName() + "is missing in the Url"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(build(GENERAL_EXCEPTION_MESSAGE));
    }

    private MessageResponse build(String message) {
        return MessageResponse.builder()
                .message(message)
                .build();
    }

}

