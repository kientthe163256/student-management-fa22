package com.example.studentmanagementfa22.exception;

import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidSortFieldException;
import com.example.studentmanagementfa22.service.TranslationService;
import com.example.studentmanagementfa22.utility.TranslationCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElement(NoSuchElementException exception) {
        String fieldCode = exception.getMessage();
        String notFoundCode = TranslationCode.NOT_FOUND;

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(fieldCode, notFoundCode);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(ElementAlreadyExistException.class)
//    public ResponseEntity handleElementExisted(ElementAlreadyExistException exception) {
//        return new ResponseEntity(new ErrorResponseDTO(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity handleUsernameNotFound(UsernameNotFoundException exception) {
//        return new ResponseEntity(new ErrorResponseDTO(exception.getMessage(), 404), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity handleException(MethodArgumentTypeMismatchException ex) {
//        String message = "Invalid input format for " + ex.getName() + ". Required type: " + ex.getRequiredType().getSimpleName();
//        return new ResponseEntity(new ErrorResponseDTO(message, 400), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorResponseDTO> responseDTOS = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String validationCode = error.getCode();
            assert validationCode != null;

            String fieldErrorCode = TranslationCode.getTranslationCode(field);
            String validationErrorCode = TranslationCode.getTranslationCode(validationCode);

            responseDTOS.add(new ErrorResponseDTO(fieldErrorCode, validationErrorCode));
        });
        return new ResponseEntity<>(responseDTOS, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity handleConstraintViolation(ConstraintViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getConstraintViolations().forEach((error) -> {
//            String fieldName = error.getPropertyPath().toString();
//            String errorMessage = error.getMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity(new ErrorResponseDTO(errors, 400), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity handleIllegalArgument(IllegalArgumentException ex) {
//        return new ResponseEntity(new ErrorResponseDTO(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(InvalidInputException.class)
//    public ResponseEntity handleInvalidInput(InvalidInputException ex) {
//        return new ResponseEntity(new ErrorResponseDTO(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(InvalidFormatException.class)
//    public ResponseEntity handleInvalidFormat(InvalidFormatException ex) {
//        String message = "Invalid format for type " + ex.getTargetType().getSimpleName() + " with given value " + ex.getValue();
//        return new ResponseEntity(new ErrorResponseDTO(message, 400), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(DisabledException.class)
//    public ResponseEntity handleDisabledException(DisabledException ex) {
//        return new ResponseEntity(new ErrorResponseDTO(ex.getMessage(), 403), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(ActionNotAllowedException.class)
//    public ResponseEntity handleNotAllowed(ActionNotAllowedException ex) {
//        return new ResponseEntity(new ErrorResponseDTO(ex.getMessage(), 403), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(InvalidSortFieldException.class)
//    public ResponseEntity handleInvalidSortField(InvalidSortFieldException ex) {
//        StringBuilder messageBuilder = new StringBuilder("Invalid field! Available fields: ");
//        for (Field f : ex.getTargetClass().getDeclaredFields()){
//            messageBuilder.append(f.getName()).append(", ");
//        }
//        String errorMessage = messageBuilder.deleteCharAt(messageBuilder.length()-2).toString();
//        return new ResponseEntity(new ErrorResponseDTO(errorMessage, 400), HttpStatus.BAD_REQUEST);
//    }
}
