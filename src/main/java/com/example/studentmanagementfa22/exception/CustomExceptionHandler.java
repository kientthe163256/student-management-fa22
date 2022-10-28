package com.example.studentmanagementfa22.exception;

import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidInputException;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    public ResponseEntity<String> handleElementExisted(ElementAlreadyExistException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleException(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid input format for " + ex.getName() + ". Required type: " + ex.getRequiredType().getSimpleName();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInput(InvalidInputException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormat(InvalidFormatException ex) {
        return new ResponseEntity<>("Invalid format for type " + ex.getTargetType().getSimpleName() + " with given value " + ex.getValue(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledException(DisabledException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<String> handleNotAllowed(ActionNotAllowedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
