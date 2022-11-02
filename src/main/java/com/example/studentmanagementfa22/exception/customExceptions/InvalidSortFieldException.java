package com.example.studentmanagementfa22.exception.customExceptions;

import lombok.Getter;

@Getter
public class InvalidSortFieldException extends RuntimeException{
    private Class targetClass;

    public InvalidSortFieldException(Class targetClass){
        this.targetClass = targetClass;
    }
}
