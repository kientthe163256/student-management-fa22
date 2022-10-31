package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.entity.ClassType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {
    private Integer id;

    private String classroomName;

    private Integer currentNoStudent;

    private Integer noStudent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date create_date;

    @Enumerated(value = EnumType.STRING)
    private ClassType classType;

    private TeacherDTO teacher;

    private SubjectDTO subject;
}
