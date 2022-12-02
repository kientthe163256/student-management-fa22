package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.service.TranslationService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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