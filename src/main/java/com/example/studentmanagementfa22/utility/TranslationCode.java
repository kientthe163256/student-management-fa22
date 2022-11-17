package com.example.studentmanagementfa22.utility;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TranslationCode {
    public static final String TEACHER_DELETED_200 = "teacher.deleted200";
    public static final String TEACHER201 = "teacher201";
    public static final String TEACHER404 = "teacher404";

    public static final String PAGE400 = "page400";

    public static final String NOTBLANK_ID = "blank.id";
    public static final String NOTBLANK_FIRSTNAME = "notblank.firstname";
    public static final String NOTBLANK_LASTNAME = "notblank.lastname";
    public static final String NOTNULL_DOB = "notnull.dob";
    public static final String PATTERN_CLASSROOMNAME = "pattern.classroomname";


    public static String getTranslationCode(String validationCode, String fieldName){
        Field[] fields = TranslationCode.class.getFields();
        List<String> codes = Arrays.stream(fields).map(field -> field.getName()).toList();

        String rawCode = validationCode.toUpperCase() + '_' + fieldName.toUpperCase();
        int index = codes.indexOf(rawCode);
        if (index != -1)
            return validationCode.toLowerCase() + '.' + fieldName.toLowerCase();
        else
            return "default";
    }
}