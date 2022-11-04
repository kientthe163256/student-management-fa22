package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponseDTO {
    private HttpStatus status;
    private String message;
}
