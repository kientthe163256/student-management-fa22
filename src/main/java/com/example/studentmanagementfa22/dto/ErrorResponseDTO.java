package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.service.TranslationService;
import com.example.studentmanagementfa22.utility.TranslationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ErrorResponseDTO {
    private String message;
    private String errorCode;

    public ErrorResponseDTO(String... errorCodes) {
        StringBuilder codeBuilder = new StringBuilder();
        StringBuilder messageBuilder = new StringBuilder();
        String space = " ";

        for (String code : errorCodes){
            codeBuilder.append(code).append(space);
            messageBuilder.append(TranslationService.toLocale(code)).append(space);
        }
        this.errorCode = codeBuilder.toString().strip();
        this.message = messageBuilder.toString().strip();
    }
}