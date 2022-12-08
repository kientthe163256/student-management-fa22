package com.example.studentmanagementfa22.utility.i18n;

import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
@NoArgsConstructor
public class MessageCode {
    //1. target object: teacher, classroom, student ...
    public static final String TEACHER = "teacher";
    public static final String CLASSROOM = "classroom";
    public static final String ACCOUNT = "account";

    public static final String STUDENT = "student";
    public static final String MARK = "mark";

    //2. fields of target object
    public static final String FIRSTNAME = "first.name";
    public static final String LASTNAME = "last.name";


    //3. status
    public static final String NOT_FOUND = "not.found";
    public static final String DELETED = "deleted";
    public static final String ADDED = "added";
    public static final String ADD_ACTION = "add.action";
    public static final String JOINED = "joined";

    public static final String EXCEEDED_AMOUNT = "exceed.amount";




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

    //7. Authorize
    public static final String NOTAUTHORIZE_CLASSROOM = "notAuthorize.classroom";
    public static final String NOTAUTHORIZE_STUDENT = "notAuthorize.student";



    public static String getTranslationCode(String validationCode){
        Field[] fields = MessageCode.class.getFields();
        List<String> codes = Arrays.stream(fields).map(Field::getName).toList();

        String rawCode = validationCode.toUpperCase();
        int index = codes.indexOf(rawCode);
        if (index != -1){
            Field field = fields[index];
            try {
                return (String) field.get(new MessageCode());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return validationCode;
    }
}