package com.example.demo.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionHandler {

    // Catch specific exception UserServiceException
    @ExceptionHandler(value = { UserServiceException.class })
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Catch all exception nullpointer, arrayindexoutofbound, etc
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleUserServiceException(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
