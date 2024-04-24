package com.nickdenningart.fractal.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nickdenningart.fractal.dto.ErrorMessage;
import com.nickdenningart.fractal.exception.AuthorizationException;
import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler{

    @ResponseBody
    @ExceptionHandler(DynamoDbItemNotFoundException.class)
    protected ResponseEntity<?> handleDynamoDbItemNotFound(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new ErrorMessage("Resource not in database"),HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<?> authorization(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new ErrorMessage("Bad API key"),HttpStatus.UNAUTHORIZED);
    }
}
