package com.example.studentmanagementfa22.utility;

import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
@NoArgsConstructor
public class TranslationCode {
    //1. target object: teacher, classroom, student ...
    public static final String TEACHER = "teacher";
    public static final String CLASSROOM = "classroom";
    public static final String ACCOUNT = "account";

    //2. fields of target object
    public static final String FIRSTNAME = "first.name";
    public static final String LASTNAME = "last.name";


    //3. status
    public static final String NOT_FOUND = "not.found";
    public static final String DELETED = "deleted";





    //4. validation annotations: NotBlank, NotNull, Min, Max ...
    public static final String NOTBLANK = "not.blank";

    public static final String NOTBLANK_ID = "blank.id";
    public static final String NOTBLANK_FIRSTNAME = "notblank.firstname";
    public static final String NOTBLANK_LASTNAME = "notblank.lastname";
    public static final String NOTNULL_DOB = "notnull.dob";
    public static final String PATTERN_CLASSROOMNAME = "pattern.classroomname";

    //5. paging
    public static final String PAGE400 = "page400";

    //6. access denied
    public static final String ACCESS_DENIED = "access.denied";


    public static String getTranslationCode(String validationCode){
        Field[] fields = TranslationCode.class.getFields();
        List<String> codes = Arrays.stream(fields).map(Field::getName).toList();

        String rawCode = validationCode.toUpperCase();
        int index = codes.indexOf(rawCode);
        if (index != -1){
            Field field = fields[index];
            try {
                return (String) field.get(new TranslationCode());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return validationCode;
    }
}