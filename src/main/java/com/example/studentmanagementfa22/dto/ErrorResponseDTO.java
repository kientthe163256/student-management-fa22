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
@AllArgsConstructor
public class ErrorResponseDTO {
    private List<String> messages;
    private String errorCode;

    public ErrorResponseDTO(String errorCode) {
        this.errorCode = errorCode;
        String translatedMessage = TranslationService.toLocale(errorCode);
        messages = new ArrayList<>();
        messages.add(translatedMessage);
    }
}