package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse<T> {
    private T errorMessage;
    private int statusCode;
}
