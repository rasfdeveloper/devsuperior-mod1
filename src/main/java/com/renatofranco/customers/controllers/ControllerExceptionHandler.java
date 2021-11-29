package com.renatofranco.customers.controllers;

import com.renatofranco.customers.controllers.expcetions.StandardError;
import com.renatofranco.customers.services.exceptions.DatabaseException;
import com.renatofranco.customers.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
            var status = HttpStatus.NOT_FOUND;

            var standardError = new StandardError(
                    Instant.now(),
                    status.value(),
                    "Resource not found",
                    e.getMessage(),
                    request.getRequestURI()
            );

            return ResponseEntity
                    .status(status)
                    .body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest request){
        var status = HttpStatus.BAD_REQUEST;

        var standardError = new StandardError(
                Instant.now(),
                status.value(),
                "Database expcetion",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(standardError);
    }

}
