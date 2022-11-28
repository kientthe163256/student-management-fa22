package com.example.studentmanagementfa22.exception.customExceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionNotAllowedException extends RuntimeException{
    public ActionNotAllowedException(String... message) {
        messages = message;
    }

    public String[] messages;
}
