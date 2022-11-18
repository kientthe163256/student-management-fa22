package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.exception.customExceptions.InvalidInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

public class PagingHelper {
    public static Map<String, Object> getCriteriaAndDirection(String sort){
        Map<String, Object> result = new HashMap<>();
        //handle invalid format
        if (!Pattern.matches("[A-Za-z]+,[A-Za-z]+", sort)){
            throw new IllegalArgumentException("Sort must be in format 'className,direction'. Ex: firstName,ASC");
        }

        //split raw input and get criteria
        String criteria = sort.split(",")[0].trim();

        //split raw input and initialize Direction (throw InvalidFormatException)
        String rawDirection = sort.split(",")[1].trim().toUpperCase();
        Sort.Direction direction = Sort.Direction.fromString(rawDirection);

        result.put("criteria", criteria);
        result.put("direction", direction);
        return result;
    }
    public static Map<String, Integer> getPaginationFields(Page<?> page, Integer pageNumber){
        Map<String, Integer> fields = new HashMap<>();
        int totalPages = page.getTotalPages();
        if (pageNumber < 1 || pageNumber > totalPages){
            throw new InvalidInputException("Page number should be in range [1," + totalPages + "]");
        }
        fields.put("first", 1);
        fields.put("previous", Math.max(pageNumber - 1, 1));
        fields.put("next", Math.min(pageNumber + 1, totalPages));
        fields.put("last", totalPages);
        fields.put("total", (int)page.getTotalElements());
        return fields;
    }

    public static boolean objectContainsField(Class object, String fieldName) {
        return Arrays.stream(object.getDeclaredFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }
    public static String objectFieldtoColumn(Class object, String fieldName) {
        for (Field field : object.getDeclaredFields()) {
            String fName = field.getName();
            if (fName.equals(fieldName)) {
                if (fName.equals("id")) {
                    return fieldName;
                }
                if (field.getAnnotation(Column.class) != null) {
                    return field.getAnnotation(Column.class).name();
                }
            }
        }
        return null;
    }

}
