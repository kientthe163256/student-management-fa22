package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDTO<T> {
    private T errorMessage;
    private int statusCode;
}
