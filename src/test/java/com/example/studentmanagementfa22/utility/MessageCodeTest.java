package com.example.studentmanagementfa22.utility;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageCodeTest {
    @Test
    public void getTranslateCode(){
        String validationCode = "NotBlank";
        String message = MessageCode.getTranslationCode(validationCode);
        Assertions.assertEquals("not.blank", message);
    }

    @Test
    public void getDefaultTranslationCode(){
        String validationCode = "NotExistCode";
        String message = MessageCode.getTranslationCode(validationCode);
        Assertions.assertEquals(validationCode, message);
    }
}
