package com.example.studentmanagementfa22.exception.customExceptions;

public class ActionNotAllowedException extends RuntimeException{
    public ActionNotAllowedException(String message) {
        super(message);
    }
}
