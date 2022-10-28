package com.example.studentmanagementfa22.exception.customExceptions;

public class ElementAlreadyExistException extends RuntimeException{
    public ElementAlreadyExistException(String message) {
        super(message);
    }
}
