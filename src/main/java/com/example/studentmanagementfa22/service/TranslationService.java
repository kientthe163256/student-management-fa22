package com.example.studentmanagementfa22.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class TranslationService {
    private static ResourceBundleMessageSource messageSource;

    public TranslationService(@Qualifier("Messages") ResourceBundleMessageSource messageSource){
        this.messageSource = messageSource;
    }

    public static String toLocale(String code){
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }
}
