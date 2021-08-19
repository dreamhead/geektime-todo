package com.github.dreamhead.todo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity error500(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(exception.getMessage(), status);
    }
}
