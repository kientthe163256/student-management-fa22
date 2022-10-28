package com.example.studentmanagementfa22.exception.customExceptions;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message) {
        super(message);
    }
}
