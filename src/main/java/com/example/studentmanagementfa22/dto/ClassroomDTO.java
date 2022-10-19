package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.entity.Teacher;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {
    private Integer id;

    private String classroomName;

    private Integer currentNoStudent;

    private Integer noStudent;

    @Enumerated(value = EnumType.STRING)
    private ClassType classType;

    private TeacherDTO teacher;

    private SubjectDTO subject;
}
